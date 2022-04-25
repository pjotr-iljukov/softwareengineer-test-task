package com.klausapp.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilsTest {

  @Test
  void parseDateFromString_correctDate_success() {
    final String date = "2019-10-21";

    var actual = DateUtils.parseDateFromString(date);

    assertThat(actual)
            .isNotNull()
            .isEqualTo(LocalDate.of(2019, 10, 21));
  }

  @Test
  void parseSqlTimestampFromString_correctTimestamp_success() {
    final String timestamp = "2019-10-21T20:37:43";

    var actual = DateUtils.parseSqlTimestampFromString(timestamp);

    assertThat(actual)
            .isNotNull()
            .isEqualTo(LocalDateTime.of(2019, 10, 21, 20, 37, 43));
  }

  @Test
  void isDailyPeriod_endExactlyFirstDayOfMonthFromStart_False() {
    LocalDate start = LocalDate.of(2022, 1, 1);
    LocalDate end = start.plusDays(31);

    boolean actual = DateUtils.isDailyPeriod(start, end);

    assertThat(actual).isFalse();
  }

  @Test
  void isDailyPeriod_startAndEndDifferentMonthsMoreThanStartMonthTotalDays_False() {
    LocalDate start = LocalDate.of(2022, 1, 1);
    LocalDate end = LocalDate.of(2022, 2, 20);

    boolean actual = DateUtils.isDailyPeriod(start, end);

    assertThat(actual).isFalse();
  }

  @Test
  void isDailyPeriod_startAndEndDifferentMonthsLessThanStartMonthTotalDays_True() {
    LocalDate start = LocalDate.of(2022, 1, 25);
    LocalDate end = LocalDate.of(2022, 2, 10);

    boolean actual = DateUtils.isDailyPeriod(start, end);

    assertThat(actual).isTrue();
  }

  @Test
  void isDailyPeriod_startAndEndSameMonth_True() {
    LocalDate start = LocalDate.of(2022, 1, 1);
    LocalDate end = LocalDate.of(2022, 1, 30);

    boolean actual = DateUtils.isDailyPeriod(start, end);

    assertThat(actual).isTrue();
  }
}
