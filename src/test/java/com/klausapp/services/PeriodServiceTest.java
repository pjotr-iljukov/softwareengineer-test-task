package com.klausapp.services;

import com.klausapp.models.RatingModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PeriodServiceTest {

  private PeriodService service;

  @BeforeEach
  void setUp() {
    service = new PeriodService();
  }

  @Test
  void getPeriods_startAndEndSameMonthMultipleRatings_dailyPeriod() {
    LocalDate start = LocalDate.of(2022, 1, 1);
    LocalDate end = LocalDate.of(2022, 1, 30);
    List<RatingModel> ratings = List.of(
            new RatingModel()
                    .setRating(5)
                    .setCreatedAt(LocalDateTime.of(2022, 1, 1, 0, 0)),
            new RatingModel()
                    .setRating(3)
                    .setCreatedAt(LocalDateTime.of(2022, 1, 2, 0, 0)),
            new RatingModel()
                    .setRating(4)
                    .setCreatedAt(LocalDateTime.of(2022, 1, 3, 0, 0)),
            new RatingModel()
                    .setRating(1)
                    .setCreatedAt(LocalDateTime.of(2022, 1, 10, 0, 0)),
            new RatingModel()
                    .setRating(5)
                    .setCreatedAt(LocalDateTime.of(2022, 1, 1, 0, 0))
    );

    var actual = service.getPeriods(ratings, start, end);

    assertThat(actual)
            .isNotNull()
            .extracting("name", "value")
            .contains(
                    tuple("2022-01-01", 40.0),
                    tuple("2022-01-02", 20.0),
                    tuple("2022-01-03", 20.0),
                    tuple("2022-01-10", 20.0));
  }

  @Test
  void getPeriods_startAndEndInDifferentMonths_weeklyPeriod() {
    LocalDate start = LocalDate.of(2022, 1, 1);
    LocalDate end = LocalDate.of(2022, 2, 28);
    List<RatingModel> ratings = List.of(
            new RatingModel()
                    .setRating(5)
                    .setCreatedAt(LocalDateTime.of(2022, 1, 1, 0, 0)),
            new RatingModel()
                    .setRating(3)
                    .setCreatedAt(LocalDateTime.of(2022, 1, 1, 0, 0)),
            new RatingModel()
                    .setRating(4)
                    .setCreatedAt(LocalDateTime.of(2022, 2, 1, 0, 0)),
            new RatingModel()
                    .setRating(4)
                    .setCreatedAt(LocalDateTime.of(2022, 2, 5, 0, 0))
    );

    var actual = service.getPeriods(ratings, start, end);

    assertThat(actual)
            .isNotNull()
            .extracting("name", "value")
            .contains(
                    tuple("Week 1", 50.0),
                    tuple("Week 6", 50.0));
  }
}
