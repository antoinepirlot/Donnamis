package be.vinci.pae.dal.services.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DALBackendService {

  PreparedStatement getPreparedStatement(String query) throws SQLException;
}
