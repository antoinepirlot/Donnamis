package be.vinci.pae.dal.services.interfaces;

import java.sql.SQLException;

public interface DALServices {

  void start() throws SQLException;

  void commit() throws SQLException;

  void rollback();
}
