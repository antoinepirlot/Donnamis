package be.vinci.pae.dal.services;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DALServices {

  PreparedStatement getPreparedStatement(String query) throws SQLException;
}
