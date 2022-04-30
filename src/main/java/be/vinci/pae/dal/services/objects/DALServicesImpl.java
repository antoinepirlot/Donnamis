package be.vinci.pae.dal.services.objects;

import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.ihm.logs.LoggerHandler;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

@Singleton
public class DALServicesImpl implements DALServices, DALBackendService {


  private final ThreadLocal<Connection> dbThreadLocal = new ThreadLocal<>();
  private final Logger logger = LoggerHandler.getLogger();
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
  public void start() throws SQLException {
    //connection = DriverManager.getConnection(url, user, dbPassword); //postgres password
    //Creating connection
    Connection connection = basicDataSource.getConnection();
    //Set the connection in the ThreadLocal Map
    connection.setAutoCommit(false);
    dbThreadLocal.set(connection);
  }

  @Override
  public void commit() throws SQLException {
    Connection connection = dbThreadLocal.get();
    connection.commit();
    connection.setAutoCommit(true);
    connection.close();
    dbThreadLocal.remove();
  }

  @Override
  public void rollback() {
    try (Connection connection = dbThreadLocal.get()) {
      connection.rollback();
      connection.setAutoCommit(true);
      connection.close();
      dbThreadLocal.remove();
    } catch (SQLException e) {
      this.logger.log(Level.SEVERE, "Impossible to join the server");
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
      this.logger.log(Level.SEVERE, "PostgreSQL driver is missing");
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
    if (connection == null) {
      logger.log(Level.SEVERE, "Connection is null");
    }
    return connection.prepareStatement(query);
  }


}
