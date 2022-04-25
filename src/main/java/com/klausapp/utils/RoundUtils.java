package com.klausapp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RoundUtils {
  private RoundUtils() {}

  public static double roundDouble(double num) {
    return new BigDecimal(num)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue();
  }
}
