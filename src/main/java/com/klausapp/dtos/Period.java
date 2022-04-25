package com.klausapp.dtos;

public class Period {
  private String name;
  private Double value;

  public Period() {
  }

  public Period(String name, Double value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Period setName(String name) {
    this.name = name;
    return this;
  }

  public Double getValue() {
    return value;
  }

  public Period setValue(Double value) {
    this.value = value;
    return this;
  }
}
