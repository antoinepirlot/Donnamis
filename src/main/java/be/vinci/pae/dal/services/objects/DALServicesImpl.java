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


  private final ThreadLocal<Connection> dbThreadLocal = new ThreadLocal<Connection>();
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
   *
   * @return the db connection
   */
  public Connection start() {
    Connection connection = null;
    try {
      //connection = DriverManager.getConnection(url, user, dbPassword); //postgres password
      //Creating connection
      connection = basicDataSource.getConnection();
      //Set the connection in the ThreadLocal Map
      connection.setAutoCommit(false);
      dbThreadLocal.set(connection);

    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
    System.out.println("Connection à la db réussie.");
    return connection;
  }

  @Override
  public boolean commit() {
    Connection connection = dbThreadLocal.get();
    try {
      connection.commit();
      connection.setAutoCommit(true);
      connection.close();
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
    return true;
  }

  @Override
  public boolean rollback() {
    Connection connection = dbThreadLocal.get();
    try {
      connection.rollback();
      connection.setAutoCommit(true);
      connection.close();
    } catch (SQLException e) {
      System.out.println("Impossible de joindre le serveur");
      System.exit(1);
    }
    return true;
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
    Connection connection = dbThreadLocal.get();
    return connection.prepareStatement(query);
  }


}
