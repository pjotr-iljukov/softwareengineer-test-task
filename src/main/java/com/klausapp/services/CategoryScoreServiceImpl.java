package com.klausapp.services;

import com.klausapp.grpc.*;
import com.klausapp.utils.DateUtils;
import io.grpc.stub.StreamObserver;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class CategoryScoreServiceImpl extends CategoryScoreServiceGrpc.CategoryScoreServiceImplBase {

  private final AggregatedCategoryScoreService scoreService;

  public CategoryScoreServiceImpl(AggregatedCategoryScoreService scoreService) {
    this.scoreService = scoreService;
  }

  @Override
  public void getCategoryScores(CategoryScoreRequest request,
                                StreamObserver<CategoryScoreResponse> responseObserver) {
    try {
      LocalDate start = DateUtils.parseDateFromString(request.getStartDate());
      LocalDate end = DateUtils.parseDateFromString(request.getEndDate());
      var scores = scoreService
              .getAggregatedCategoryScoresByPeriod(start, end)
              .stream()
              .map(s -> {
                var builder = AggregatedCategoryScoreResponse
                        .newBuilder()
                        .setCategory(s.getCategory())
                        .setRatings(s.getRatings())
                        .setScore(s.getScore());
                var periods = s.getPeriods()
                        .stream()
                        .map(p -> AggregatedCategoryScorePeriodResponse
                                .newBuilder()
                                .setName(p.getName())
                                .setValue(p.getValue())
                                .build())
                        .collect(Collectors.toList());
                builder.addAllPeriods(periods);
                return builder.build();
              }).collect(Collectors.toList());
      var response = CategoryScoreResponse
              .newBuilder()
              .addAllScores(scores)
              .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      System.out.println("Error getting aggregated score values. " + e.getMessage());
      responseObserver.onError(e);
    }
  }

  @Override
  public void getTicketScores(TicketScoreRequest request, StreamObserver<TicketScoreResponse> responseObserver) {
    try {
      LocalDate start = DateUtils.parseDateFromString(request.getStartDate());
      LocalDate end = DateUtils.parseDateFromString(request.getEndDate());
      var scores = scoreService.getTicketScoresByPeriod(start, end)
              .stream()
              .map(s -> AggregatedTicketScoreResponse
                      .newBuilder()
                      .setTicketID(s.getTicketId())
                      .setCategory(s.getCategoryName())
                      .setScore(s.getScoreValue())
                      .build())
              .collect(Collectors.toList());
      var response = TicketScoreResponse
              .newBuilder()
              .addAllTicketScores(scores)
              .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void getOverallQualityScore(OverallQualityScoreRequest request,
                                     StreamObserver<OverallQualityScoreResponse> responseObserver) {
    try {
      LocalDate start = DateUtils.parseDateFromString(request.getStartDate());
      LocalDate end = DateUtils.parseDateFromString(request.getEndDate());
      var overallScore = scoreService.getOverallQualityScoreByPeriod(start, end);
      var response = OverallQualityScoreResponse
              .newBuilder()
              .setOverallScore(overallScore.getScoreValue())
              .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void getScoreChangeOverPeriod(ScoreChangeOverPeriodRequest request,
                                       StreamObserver<ScoreChangeOverPeriodResponse> responseObserver) {
    try {
      LocalDate firstPeriodStart = DateUtils.parseDateFromString(request.getFirstPeriodStart());
      LocalDate firstPeriodEnd = DateUtils.parseDateFromString(request.getFirstPeriodEnd());
      LocalDate secondPeriodStart = DateUtils.parseDateFromString(request.getSecondPeriodStart());
      LocalDate secondPeriodEnd = DateUtils.parseDateFromString(request.getSecondPeriodEnd());
      var scoreChangeOverPeriod = scoreService
              .getScoreChangeOverPeriod(firstPeriodStart, firstPeriodEnd, secondPeriodStart, secondPeriodEnd);
      var response = ScoreChangeOverPeriodResponse
              .newBuilder()
              .setScoreChange(scoreChangeOverPeriod.getScoreChange())
              .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }
}
