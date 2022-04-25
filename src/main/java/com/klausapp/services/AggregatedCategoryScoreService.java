package com.klausapp.services;

import com.klausapp.dtos.AggregatedCategoryScore;
import com.klausapp.dtos.OverallQualityScore;
import com.klausapp.dtos.ScoreChangeOverPeriod;
import com.klausapp.dtos.TicketAggregatedCategoryScore;
import com.klausapp.models.RatingModel;
import com.klausapp.repositories.RatingCategoriesRepository;
import com.klausapp.repositories.RatingRepository;
import com.klausapp.utils.RoundUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AggregatedCategoryScoreService {

  private final RatingRepository ratingRepository;
  private final RatingCategoriesRepository ratingCategoriesRepository;
  private final PeriodService periodService;

  public AggregatedCategoryScoreService(RatingRepository ratingRepository,
                                        RatingCategoriesRepository ratingCategoriesRepository,
                                        PeriodService periodService) {
    this.ratingRepository = ratingRepository;
    this.ratingCategoriesRepository = ratingCategoriesRepository;
    this.periodService = periodService;
  }

  public List<AggregatedCategoryScore> getAggregatedCategoryScoresByPeriod(LocalDate start, LocalDate end)
          throws SQLException {
    var ratings = ratingRepository.findByCreatedAtBetween(start, end);
    return ratingCategoriesRepository.findAll()
            .stream()
            .map(c -> {
              var categoryRatings = ratings
                      .stream()
                      .filter(r -> c.getId().equals(r.getRatingCategoryId()))
                      .collect(Collectors.toList());

              var average = categoryRatings
                      .stream()
                      .mapToInt(RatingModel::getRating)
                      .average()
                      .orElse(0.0);
              if (average > 0.0) {
                return new AggregatedCategoryScore()
                        .setCategory(c.getName())
                        .setRatings(categoryRatings.size())
                        .setScore(getScoreInPercents(average, ratings.size(), c.getWeight()))
                        .setPeriods(periodService.getPeriods(categoryRatings, start, end));
              }
              return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
  }

  public List<TicketAggregatedCategoryScore> getTicketScoresByPeriod(LocalDate start, LocalDate end) throws SQLException {
    var response = new ArrayList<TicketAggregatedCategoryScore>();
    var categories = ratingCategoriesRepository.findAll();
    var ratings = ratingRepository.findByCreatedAtBetween(start, end);
    var uniqueTicketIds = ratings
            .stream()
            .map(RatingModel::getTicketId)
            .collect(Collectors.toSet());

    for (var id : uniqueTicketIds) {
      for (var c : categories) {
        var average = ratings
                .stream()
                .filter(r -> r.getTicketId().equals(id) && r.getRatingCategoryId().equals(c.getId()))
                .mapToInt(RatingModel::getRating)
                .average()
                .orElse(0.0);
        if (average > 0.0) {
          var score = getScoreInPercents(average, ratings.size(), c.getWeight());
          response
                  .add(new TicketAggregatedCategoryScore(id, c.getName(), score));
        }
      }
    }
    return response;
  }

  public OverallQualityScore getOverallQualityScoreByPeriod(LocalDate start, LocalDate end) throws SQLException {
    var overallScore = getAggregatedCategoryScoresByPeriod(start, end)
            .stream()
            .mapToDouble(AggregatedCategoryScore::getScore)
            .sum();
    return new OverallQualityScore(RoundUtils.roundDouble(overallScore));
  }

  public ScoreChangeOverPeriod getScoreChangeOverPeriod(LocalDate firstPeriodStart, LocalDate firstPeriodEnd,
                                                        LocalDate secondPeriodStart, LocalDate secondPeriodEnd) throws SQLException {
    var firstPeriodOverallScore = getOverallQualityScoreByPeriod(firstPeriodStart, firstPeriodEnd);
    var secondPeriodOverallScore = getOverallQualityScoreByPeriod(secondPeriodStart, secondPeriodEnd);
    var change = secondPeriodOverallScore.getScoreValue() - firstPeriodOverallScore.getScoreValue();
    return new ScoreChangeOverPeriod(RoundUtils.roundDouble(change));
  }

  private double getScoreInPercents(double avg, int totalCount, float weight) {
    var score = ((avg / totalCount) * weight) * 100;
    return RoundUtils.roundDouble(score);
  }
}
