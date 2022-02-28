package be.vinci.pae.services;

import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Singleton
class DALServices {

  private final Connection connection = dbConnection();


  /**
   * Try to connect to the db
   *
   * @return the db connection
   */
  private Connection dbConnection() {
    driverLoading();
    String url = "jdbc:postgresql://localhost:5432/postgres";
    Connection connection = null;
    try {
      connection = DriverManager.getConnection(url, "postgres",
          "Postgresql001"); //postgres password
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
    System.out.println("Réussis");
    return connection;
  }

  /**
   * Load the driver for PostgreSQL.
   */
  private void driverLoading() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Driver PostgreSQL manquant");
      System.exit(1);
    }
  }

  /**
   * Create a new preparedStatement with the query.
   *
   * @param query the query for the preparedStatement
   * @return the new preparedStatement
   * @throws SQLException if SQL error
   */
  public PreparedStatement getPreparedStatement(String query) throws SQLException {
    return connection.prepareStatement(query);
  }
}
