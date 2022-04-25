package com.klausapp.services;

import com.klausapp.dtos.Period;
import com.klausapp.models.RatingModel;
import com.klausapp.utils.DateUtils;
import com.klausapp.utils.RoundUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PeriodService {
  public List<Period> getPeriods(List<RatingModel> ratings, LocalDate start, LocalDate end) {
    if (DateUtils.isDailyPeriod(start, end)) {
      return getDailyPeriods(ratings, start, end);
    }
    return getWeeklyPeriods(ratings, start, end);
  }

  private List<Period> getWeeklyPeriods(List<RatingModel> ratings, LocalDate start, LocalDate end) {
    var result = new ArrayList<Period>();
    int weekCount = 1;
    boolean isFinished = false;
    while (!isFinished) {
      final var step = start;
      var endOfWeek = start
              .plusDays(dayToEndOfTheWeek(start));
      // TODO tests
      var count = ratings
              .stream()
              .filter(r -> (r.getCreatedAt().toLocalDate().isEqual(step) || r.getCreatedAt().toLocalDate().isAfter(step))
                      && (r.getCreatedAt().toLocalDate().isEqual(endOfWeek) || r.getCreatedAt().toLocalDate().isBefore(endOfWeek)))
              .count();
      if (count > 0) {
        result
                .add(new Period(String.format("Week %s", weekCount), getPeriodValue(count, ratings.size())));
      }
      if (endOfWeek.isAfter(end)) {
        isFinished = true;
      } else {
        start = endOfWeek.plusDays(1);
        weekCount++;
      }
    }
    return result;
  }

  private List<Period> getDailyPeriods(List<RatingModel> ratings, LocalDate start, LocalDate end) {
    var response = new ArrayList<Period>();
    while (start.isBefore(end)) {
      final LocalDate step = start;
      var count = ratings
              .stream()
              .filter(r -> step.equals(r.getCreatedAt().toLocalDate()))
              .count();
      if (count > 0) {
        response
                .add(new Period(start.toString(), getPeriodValue(count, ratings.size())));
      }
      start = start.plusDays(1);
    }
    return response;
  }

  private int dayToEndOfTheWeek(LocalDate date) {
    return DayOfWeek.SUNDAY.getValue() - date.getDayOfWeek().getValue();
  }

  private double getPeriodValue(long count, int totalCount) {
    var value = (double) count / totalCount * 100;
    return RoundUtils.roundDouble(value);
  }
}
