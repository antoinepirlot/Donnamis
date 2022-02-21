package be.vinci.pae.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DALServices {

  private final Connection conn = dbConnection();

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
}
