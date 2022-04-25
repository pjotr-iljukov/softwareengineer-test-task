package com.klausapp.dtos;

public class OverallQualityScore {

  private double scoreValue;

  public OverallQualityScore() {
  }

  public OverallQualityScore(double scoreValue) {
    this.scoreValue = scoreValue;
  }

  public double getScoreValue() {
    return scoreValue;
  }

  public OverallQualityScore setScoreValue(double scoreValue) {
    this.scoreValue = scoreValue;
    return this;
  }
}
