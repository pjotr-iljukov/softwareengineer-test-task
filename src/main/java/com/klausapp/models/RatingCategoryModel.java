package com.klausapp.models;

public class RatingCategoryModel {
  private Integer id;
  private String name;
  private Float weight;

  public RatingCategoryModel() {
  }

  public RatingCategoryModel(Integer id, String name, Float weight) {
    this.id = id;
    this.name = name;
    this.weight = weight;
  }

  public Integer getId() {
    return id;
  }

  public RatingCategoryModel setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public RatingCategoryModel setName(String name) {
    this.name = name;
    return this;
  }

  public Float getWeight() {
    return weight;
  }

  public RatingCategoryModel setWeight(Float weight) {
    this.weight = weight;
    return this;
  }
}
