package com.klausapp.repositories;

import com.klausapp.models.RatingModel;
import com.klausapp.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RatingRepositoryImpl implements RatingRepository {

  private static final String QUERY = "SELECT * FROM ratings WHERE created_at BETWEEN ? AND ?";
  private final Connection connection;

  public RatingRepositoryImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<RatingModel> findByCreatedAtBetween(LocalDate start, LocalDate end)
          throws SQLException {
    List<RatingModel> ratings = new ArrayList<>();
    try (PreparedStatement st = connection.prepareStatement(QUERY)) {
      st.setString(1, start.toString());
      st.setString(2, end.toString());
      var rs = st.executeQuery();
      while (rs.next()) {
        ratings.add(toRatingModel(rs));
      }
    }
    return ratings;
  }

  private RatingModel toRatingModel(ResultSet rs) throws SQLException {
    return new RatingModel()
            .setId(rs.getInt("id"))
            .setRating(rs.getInt("rating"))
            .setTicketId(rs.getInt("ticket_id"))
            .setRatingCategoryId(rs.getInt("rating_category_id"))
            .setReviewerId(rs.getInt("reviewer_id"))
            .setRevieweeId(rs.getInt("reviewee_id"))
            .setCreatedAt(DateUtils.parseSqlTimestampFromString(rs.getString("created_at")));
  }
}
