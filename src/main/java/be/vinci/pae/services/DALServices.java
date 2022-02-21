package be.vinci.pae.services;

import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Singleton
class DALServices {

  private final Connection connection = dbConnection();

  
  private Connection dbConnection() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant");
      System.exit(1);
    }
    String url = "jdbc:postgresql://localhost:5432/postgres";
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(url, "postgres", "nikesakou11"); //postgres password
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
    System.out.println("Réussis");
    return connection;
  }

  public PreparedStatement getPreparedStatement(String query) throws SQLException {
    return connection.prepareStatement(query);
  }
}
