package be.vinci.pae.dal.services.objects;

import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

@Singleton
public class DALServicesImpl implements DALServices, DALBackendService {


  private final ThreadLocal<Connection> dbThreadLocal = new ThreadLocal<>();
  private final BasicDataSource basicDataSource = setBasicDataSource();

  private BasicDataSource setBasicDataSource() {
    BasicDataSource ds = new BasicDataSource();
    driverLoading();
    String url = Config.getProperty("url");
    String user = Config.getProperty("user");
    String dbPassword = Config.getProperty("dbpassword");
    ds.setUsername(user);
    ds.setUrl(url);
    ds.setPassword(dbPassword);
    return ds;
  }

  /**
   * Try to connect to the db.
   */
  public void start() {
    //connection = DriverManager.getConnection(url, user, dbPassword); //postgres password
    //Creating connection
    try (Connection connection = basicDataSource.getConnection()) {
      System.out.println("Connexion prise par le thread : " + connection);
      //Set the connection in the ThreadLocal Map
      connection.setAutoCommit(false);
      dbThreadLocal.set(connection);
      System.out.println("Connection à la db réussie.");
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
  }

  @Override
  public void commit() {
    try (Connection connection = dbThreadLocal.get()) {
      connection.commit();
      connection.setAutoCommit(true);
      connection.close();
      dbThreadLocal.remove();
      System.out.println("Connection removed");
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
  }

  @Override
  public void rollback() {
    try (Connection connection = dbThreadLocal.get()) {
      connection.rollback();
      connection.setAutoCommit(true);
      connection.close();
      dbThreadLocal.remove();
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
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
    System.out.println("Récupération de la connection du thread courant");
    Connection connection = dbThreadLocal.get();
    System.out.println(connection);
    System.out.println("Preparation du query");
    return connection.prepareStatement(query);
  }


}
