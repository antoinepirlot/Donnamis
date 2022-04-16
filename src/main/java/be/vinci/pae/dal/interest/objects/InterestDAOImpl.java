package be.vinci.pae.dal.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InterestDAOImpl implements InterestDAO {

  @Inject
  private DALBackendService dalBackendService;

  @Override
  public boolean markInterest(InterestDTO interestDTO) throws SQLException {
    String query = "INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date) "
        + "VALUES (?, ?, ?, ?);";
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setBoolean(1, interestDTO.isCallWanted());
      ps.setInt(2, interestDTO.getOffer().getId());
      ps.setInt(3, interestDTO.getMember().getId());
      ps.setTimestamp(4, interestDTO.getDate());
      int res = ps.executeUpdate();
      if (res != 0) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean interestExist(InterestDTO interestDTO) throws SQLException {
    String query = "SELECT id_interest "
        + "FROM project_pae.interests "
        + "WHERE id_offer = ? "
        + "  AND id_member = ?";
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, interestDTO.getOffer().getId());
      ps.setInt(2, interestDTO.getMember().getId());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return true;
        }
      }
    }
    return false;
  }
}
