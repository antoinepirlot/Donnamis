package be.vinci.pae.dal.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.text.StringEscapeUtils;

public class InterestDAOImpl implements InterestDAO {

  @Inject
  private DALBackendService dalBackendService;

  @Override
  public boolean markInterest(InterestDTO interestDTO) {
    String query = "INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date, "
        + "version_interest) "
        + "VALUES (?, ?, ?, ?, 1); "
        + "UPDATE project_pae.offers "
        + "SET number_of_interests = number_of_interests + 1, "
        + "    version_offer = version_offer + 1 "
        + "WHERE id_offer = ?;";
    if (interestDTO.isCallWanted()) {
      query += "UPDATE project_pae.members "
          + "SET phone = ?, version_member = version_member + 1 "
          + "WHERE id_member = ?;";
    }
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setBoolean(1, interestDTO.isCallWanted());
      ps.setInt(2, interestDTO.getOffer().getId());
      ps.setInt(3, interestDTO.getMember().getId());
      ps.setTimestamp(4, interestDTO.getDate());
      ps.setInt(5, interestDTO.getOffer().getId());
      if (interestDTO.isCallWanted()) {
        ps.setString(6, StringEscapeUtils
            .escapeHtml4(interestDTO.getMember().getPhoneNumber()));
        ps.setInt(7, interestDTO.getMember().getId());
      }
      return ps.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean interestExist(InterestDTO interestDTO) {
    String query = "SELECT id_interest "
        + "FROM project_pae.interests "
        + "WHERE id_offer = ? "
        + "  AND id_member = ?";
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, interestDTO.getOffer().getId());
      ps.setInt(2, interestDTO.getMember().getId());
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
