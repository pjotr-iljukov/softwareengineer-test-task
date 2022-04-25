package com.klausapp.repositories;

import com.klausapp.models.RatingCategoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RatingCategoriesRepositoryImpl implements RatingCategoriesRepository {

  private static final String QUERY = "SELECT * FROM rating_categories";
  private final Connection connection;

  public RatingCategoriesRepositoryImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<RatingCategoryModel> findAll() throws SQLException {
    List<RatingCategoryModel> result = new ArrayList<>();
    try (Statement s = connection.createStatement()) {
      var rs = s.executeQuery(QUERY);
      while (rs.next()) {
        result.add(toRatingCategoryModel(rs));
      }
    }
    return result;
  }

  private RatingCategoryModel toRatingCategoryModel(ResultSet rs) throws SQLException {
    return new RatingCategoryModel()
            .setId(rs.getInt("id"))
            .setName(rs.getString("name"))
            .setWeight(rs.getFloat("weight"));
  }
}
