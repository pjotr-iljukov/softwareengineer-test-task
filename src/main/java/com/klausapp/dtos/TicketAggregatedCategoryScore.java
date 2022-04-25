package com.klausapp.dtos;

public class TicketAggregatedCategoryScore {

  private Integer ticketId;
  private String categoryName;
  private Double scoreValue;

  public TicketAggregatedCategoryScore() {
  }

  public TicketAggregatedCategoryScore(Integer ticketId, String categoryName, Double scoreValue) {
    this.ticketId = ticketId;
    this.categoryName = categoryName;
    this.scoreValue = scoreValue;
  }

  public Integer getTicketId() {
    return ticketId;
  }

  public TicketAggregatedCategoryScore setTicketId(Integer ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public TicketAggregatedCategoryScore setCategoryName(String categoryName) {
    this.categoryName = categoryName;
    return this;
  }

  public Double getScoreValue() {
    return scoreValue;
  }

  public TicketAggregatedCategoryScore setScoreValue(Double scoreValue) {
    this.scoreValue = scoreValue;
    return this;
  }
}
