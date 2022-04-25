package com.klausapp.dtos;

import java.util.ArrayList;
import java.util.List;

public class AggregatedCategoryScore {

  private String category;
  private Integer ratings;
  private Double score;
  private List<Period> periods = new ArrayList<>();

  public List<Period> addPeriod(Period p) {
    periods.add(p);
    return periods;
  }

  public String getCategory() {
    return category;
  }

  public AggregatedCategoryScore setCategory(String category) {
    this.category = category;
    return this;
  }

  public Integer getRatings() {
    return ratings;
  }

  public AggregatedCategoryScore setRatings(Integer ratings) {
    this.ratings = ratings;
    return this;
  }

  public Double getScore() {
    return score;
  }

  public AggregatedCategoryScore setScore(Double score) {
    this.score = score;
    return this;
  }

  public List<Period> getPeriods() {
    return periods;
  }

  public AggregatedCategoryScore setPeriods(List<Period> periods) {
    this.periods = periods;
    return this;
  }
}
