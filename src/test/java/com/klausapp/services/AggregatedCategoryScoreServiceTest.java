package com.klausapp.services;

import com.klausapp.models.RatingCategoryModel;
import com.klausapp.models.RatingModel;
import com.klausapp.repositories.RatingCategoriesRepository;
import com.klausapp.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AggregatedCategoryScoreServiceTest {
  @Mock
  private RatingRepository ratingRepository;
  @Mock
  private RatingCategoriesRepository ratingCategoriesRepository;
  @Mock
  private PeriodService periodService;

  private AggregatedCategoryScoreService service;

  @BeforeEach
  void setUp() {
    service = new AggregatedCategoryScoreService(ratingRepository, ratingCategoriesRepository, periodService);
  }

  @Test
  void getScoreChangeOverPeriod_twoPeriodsMultipleCategoriesMultipleRatings_success() throws SQLException {
    var firstPeriodStart = LocalDate.of(2022, 1, 1);
    var firstPeriodEnd = LocalDate.of(2022, 1, 15);
    var secondPeriodStart = LocalDate.of(2022, 2, 1);
    var secondPeriodEnd = LocalDate.of(2022, 2, 15);
    when(ratingCategoriesRepository.findAll()).thenReturn(List.of(
            new RatingCategoryModel(1, "Category 1", 0.1f),
            new RatingCategoryModel(2, "Category 2", 0.25f),
            new RatingCategoryModel(3, "Category 3", 0.25f),
            new RatingCategoryModel(4, "Category 4", 0.25f)
    ));
    when(ratingRepository.findByCreatedAtBetween(firstPeriodStart, firstPeriodEnd)).thenReturn(List.of(
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(1)
                    .setRating(5),
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(2)
                    .setRating(3)
    ));
    when(ratingRepository.findByCreatedAtBetween(secondPeriodStart, secondPeriodEnd))
            .thenReturn(List.of(new RatingModel()
                            .setTicketId(1)
                            .setRatingCategoryId(3)
                            .setRating(4),
                    new RatingModel()
                            .setTicketId(1)
                            .setRatingCategoryId(4)
                            .setRating(4)));

    var actual = service
            .getScoreChangeOverPeriod(firstPeriodStart, firstPeriodEnd, secondPeriodStart, secondPeriodEnd);

    assertThat(actual)
            .isNotNull()
            .extracting("scoreChange")
            .isEqualTo(37.5);
  }

  @Test
  void getOverallQualityScoreByPeriod_multipleCategories_success() throws SQLException {
    var startDate = LocalDate.of(2022, 1, 1);
    var endDate = LocalDate.of(2022, 1, 15);
    when(ratingCategoriesRepository.findAll()).thenReturn(List.of(
            new RatingCategoryModel(1, "Category 1", 0.1f),
            new RatingCategoryModel(2, "Category 2", 0.25f),
            new RatingCategoryModel(3, "Category 3", 0.25f),
            new RatingCategoryModel(4, "Category 4", 0.25f)
    ));
    when(ratingRepository.findByCreatedAtBetween(startDate, endDate)).thenReturn(List.of(
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(1)
                    .setRating(5),
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(2)
                    .setRating(3),
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(3)
                    .setRating(4),
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(4)
                    .setRating(4)
    ));

    var actual = service.getOverallQualityScoreByPeriod(startDate, endDate);

    assertThat(actual)
            .isNotNull()
            .extracting( "scoreValue")
            .isEqualTo(81.25);
  }

  @Test
  void getTicketScoresByPeriod_multipleTicketsForMultipleCategories_success() throws SQLException {
    var startDate = LocalDate.of(2022, 1, 1);
    var endDate = LocalDate.of(2022, 1, 15);
    when(ratingCategoriesRepository.findAll()).thenReturn(List.of(
            new RatingCategoryModel(1, "Category 1", 0.25f),
            new RatingCategoryModel(2, "Category 2", 0.25f)
    ));
    when(ratingRepository.findByCreatedAtBetween(startDate, endDate)).thenReturn(List.of(
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(1)
                    .setRating(5),
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(2)
                    .setRating(3),
            new RatingModel()
                    .setTicketId(2)
                    .setRatingCategoryId(1)
                    .setRating(4),
            new RatingModel()
                    .setTicketId(2)
                    .setRatingCategoryId(2)
                    .setRating(4)
    ));

    var actual = service.getTicketScoresByPeriod(startDate, endDate);

    assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .extracting("ticketId", "categoryName", "scoreValue")
            .contains(
                    tuple(1, "Category 1", 31.25),
                    tuple(1, "Category 2", 18.75),
                    tuple(2, "Category 1", 25.0),
                    tuple(2, "Category 2", 25.0));
  }

  @Test
  void getTicketScoresByPeriod_oneTicketForMultipleCategories_success() throws SQLException {
    var startDate = LocalDate.of(2022, 1, 1);
    var endDate = LocalDate.of(2022, 1, 15);
    when(ratingCategoriesRepository.findAll()).thenReturn(List.of(
            new RatingCategoryModel(1, "Category 1", 0.1f),
            new RatingCategoryModel(2, "Category 2", 0.25f),
            new RatingCategoryModel(3, "Category 3", 0.25f),
            new RatingCategoryModel(4, "Category 4", 0.25f)
    ));
    when(ratingRepository.findByCreatedAtBetween(startDate, endDate)).thenReturn(List.of(
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(1)
                    .setRating(5),
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(2)
                    .setRating(3),
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(3)
                    .setRating(4),
            new RatingModel()
                    .setTicketId(1)
                    .setRatingCategoryId(4)
                    .setRating(4)
    ));

    var actual = service.getTicketScoresByPeriod(startDate, endDate);

    assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .extracting("ticketId", "categoryName", "scoreValue")
            .contains(
                    tuple(1, "Category 1", 12.5),
                    tuple(1, "Category 2", 18.75),
                    tuple(1, "Category 3", 25.0),
                    tuple(1, "Category 4", 25.0));
  }

  @Test
  void getAggregatedScore_sameMonthMultipleCategories_success() throws SQLException {
    var startDate = LocalDate.of(2022, 1, 1);
    var endDate = LocalDate.of(2022, 1, 30);
    when(ratingCategoriesRepository.findAll()).thenReturn(List.of(
            new RatingCategoryModel(1, "Category 1", 0.1f),
            new RatingCategoryModel(2, "Category 2", 0.15f)
    ));
    when(ratingRepository.findByCreatedAtBetween(startDate, endDate)).thenReturn(List.of(
            new RatingModel()
                    .setRatingCategoryId(1)
                    .setRating(5),
            new RatingModel()
                    .setRatingCategoryId(2)
                    .setRating(3),
            new RatingModel()
                    .setRatingCategoryId(2)
                    .setRating(4)
    ));


    var actual = service.getAggregatedCategoryScoresByPeriod(startDate, endDate);

    assertThat(actual)
            .isNotNull()
            .extracting("category", "ratings", "score")
            .contains(
                    tuple("Category 1", 1, 16.67),
                    tuple("Category 2", 2, 17.5));
    verify(periodService, times(2)).getPeriods(anyList(), any(LocalDate.class), any(LocalDate.class));
  }


  @Test
  void getAggregatedScore_sameMonthOneCategoryMultipleRatings_success() throws SQLException {
    var startDate = LocalDate.of(2022, 1, 1);
    var endDate = LocalDate.of(2022, 1, 30);
    when(ratingCategoriesRepository.findAll()).thenReturn(List.of(
            new RatingCategoryModel(1, "Category 1", 0.1f)
    ));
    when(ratingRepository.findByCreatedAtBetween(startDate, endDate)).thenReturn(List.of(
            new RatingModel()
                    .setRatingCategoryId(1)
                    .setRating(5),
            new RatingModel()
                    .setRatingCategoryId(1)
                    .setRating(3),
            new RatingModel()
                    .setRatingCategoryId(1)
                    .setRating(4),
            new RatingModel()
                    .setRatingCategoryId(1)
                    .setRating(1),
            new RatingModel()
                    .setRatingCategoryId(1)
                    .setRating(5)
    ));


    var actual = service.getAggregatedCategoryScoresByPeriod(startDate, endDate);

    assertThat(actual)
            .isNotNull()
            .extracting("category", "ratings", "score")
            .contains(tuple("Category 1", 5, 7.2));
    verify(periodService).getPeriods(anyList(), any(LocalDate.class), any(LocalDate.class));
  }
}
