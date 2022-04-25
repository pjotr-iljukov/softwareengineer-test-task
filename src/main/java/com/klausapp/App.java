package com.klausapp;

import com.klausapp.repositories.RatingCategoriesRepositoryImpl;
import com.klausapp.repositories.RatingRepositoryImpl;
import com.klausapp.services.AggregatedCategoryScoreService;
import com.klausapp.services.CategoryScoreServiceImpl;
import com.klausapp.services.PeriodService;
import io.grpc.ServerBuilder;

import java.sql.Connection;
import java.sql.DriverManager;

public class App {

  public static void main(String[] args) {
    try (Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db")) {
      var ratingRepository = new RatingRepositoryImpl(connection);
      var categoriesRepository = new RatingCategoriesRepositoryImpl(connection);
      var periodService = new PeriodService();
      var aggregatedCategoryScoreService =
              new AggregatedCategoryScoreService(ratingRepository, categoriesRepository, periodService);

      var server = ServerBuilder
              .forPort(9090)
              .addService(new CategoryScoreServiceImpl(aggregatedCategoryScoreService))
              .build();

      server.start();
      server.awaitTermination();

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

}
