package com.klausapp.dtos;

public class ScoreChangeOverPeriod {

  private Double scoreChange;

  public ScoreChangeOverPeriod() {
  }

  public ScoreChangeOverPeriod(Double scoreChange) {
    this.scoreChange = scoreChange;
  }

  public Double getScoreChange() {
    return scoreChange;
  }

  public ScoreChangeOverPeriod setScoreChange(Double scoreChange) {
    this.scoreChange = scoreChange;
    return this;
  }
}
