package be.vinci.pae.dal.refusal.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.dal.refusal.interfaces.RefusalDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.text.StringEscapeUtils;

public class RefusalDAOImpl implements RefusalDAO {

  @Inject
  private DALBackendService dalBackendService;
  @Inject
  private Factory factory;

  @Override
  public RefusalDTO getRefusal(String username) throws SQLException {
    String query = "SELECT r.id_refusal, "
        + "                r.text "
        + "FROM project_pae.refusals r, "
        + "     project_pae.members m "
        + "WHERE r.id_member = m.id_member "
        + "  AND m.username = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(username));
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return ObjectsInstanceCreator.createRefusalInstance(this.factory, rs);
        }
        return null;
      }
    }
  }
}
