package be.vinci.pae.dal.services.objects;

import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Singleton
public class DALServicesImpl implements DALServices {

  private final Connection connection = dbConnection();


  /**
   * Try to connect to the db.
   *
   * @return the db connection
   */
  private Connection dbConnection() {
    driverLoading();
    Connection connection = null;
    String url = Config.getProperty("url");
    String user = Config.getProperty("user");
    String dbPassword = Config.getProperty("dbpassword");
    try {
      connection = DriverManager.getConnection(url, user, dbPassword); //postgres password
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
    System.out.println("Connection à la db réussie.");
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
  @Override
  public PreparedStatement getPreparedStatement(String query) throws SQLException {
    return connection.prepareStatement(query);
  }
}
