package com.klausapp.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  private static final String TIMESTAMP_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
  private static final String DATE_PATTERN = "yyyy-MM-dd";

  private DateUtils() {}
  public static boolean isDailyPeriod(LocalDate start, LocalDate end) {
    if (isSameMonth(start, end)) {
      return true;
    }
    return isWeeklyPeriod(start, end);
  }

  public static LocalDateTime parseSqlTimestampFromString(String sqlTimestamp) {
    return LocalDateTime
            .parse(sqlTimestamp, DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN));
  }

  public static LocalDate parseDateFromString(String dateString) {
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(DATE_PATTERN));
  }

  private static boolean isWeeklyPeriod(LocalDate start, LocalDate end) {
    long daysBetween = Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
    int totalDaysInCurrentMonth = YearMonth.of(start.getYear(), start.getMonth()).lengthOfMonth();
    return daysBetween < totalDaysInCurrentMonth;
  }

  private static boolean isSameMonth(LocalDate start, LocalDate end) {
    return start.getMonth().equals(end.getMonth());
  }

}
