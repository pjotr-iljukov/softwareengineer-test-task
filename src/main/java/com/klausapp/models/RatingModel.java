package com.klausapp.models;

import java.time.LocalDateTime;

public class RatingModel {
  private Integer id;
  private Integer rating;
  private Integer ticketId;
  private Integer ratingCategoryId;
  private Integer reviewerId;
  private Integer revieweeId;
  private LocalDateTime createdAt;

  public Integer getId() {
    return id;
  }

  public RatingModel setId(Integer id) {
    this.id = id;
    return this;
  }

  public Integer getRating() {
    return rating;
  }

  public RatingModel setRating(Integer rating) {
    this.rating = rating;
    return this;
  }

  public Integer getTicketId() {
    return ticketId;
  }

  public RatingModel setTicketId(Integer ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  public Integer getRatingCategoryId() {
    return ratingCategoryId;
  }

  public RatingModel setRatingCategoryId(Integer ratingCategoryId) {
    this.ratingCategoryId = ratingCategoryId;
    return this;
  }

  public Integer getReviewerId() {
    return reviewerId;
  }

  public RatingModel setReviewerId(Integer reviewerId) {
    this.reviewerId = reviewerId;
    return this;
  }

  public Integer getRevieweeId() {
    return revieweeId;
  }

  public RatingModel setRevieweeId(Integer revieweeId) {
    this.revieweeId = revieweeId;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public RatingModel setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }
}
