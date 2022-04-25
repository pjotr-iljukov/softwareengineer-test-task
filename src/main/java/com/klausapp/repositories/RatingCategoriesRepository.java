package com.klausapp.repositories;

import com.klausapp.models.RatingCategoryModel;

import java.sql.SQLException;
import java.util.List;

public interface RatingCategoriesRepository {
  List<RatingCategoryModel> findAll() throws SQLException;
}

