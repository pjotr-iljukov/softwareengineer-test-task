package com.klausapp.repositories;

import com.klausapp.models.RatingModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RatingRepository {

  List<RatingModel> findByCreatedAtBetween(LocalDate start, LocalDate end) throws SQLException;

}
