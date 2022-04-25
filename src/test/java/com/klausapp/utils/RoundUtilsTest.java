package com.klausapp.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoundUtilsTest {

  @Test
  void roundDouble_doubleValue_roundedTo2DecimalsUp() {
    double testValue = 2.14556;

    double actual = RoundUtils.roundDouble(testValue);

    assertThat(actual).isEqualTo(2.15);
  }

  @Test
  void roundDouble_doubleValue_roundedTo2DecimalsDown() {
    double testValue = 2.1444;

    double actual = RoundUtils.roundDouble(testValue);

    assertThat(actual).isEqualTo(2.14);
  }
}
