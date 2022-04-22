package be.vinci.pae.dal.recipient.objects;

import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.dal.recipient.interfaces.RecipientDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.text.StringEscapeUtils;

public class RecipientDAOImpl implements RecipientDAO {

  private static final String RECEIVED_DEFAULT = "waiting";
  private static final String NOT_RECEIVED = "not received";
  private static final String RECEIVED = "received";
  @Inject
  private DALBackendService dalBackendService;

  @Override
  public boolean chooseRecipient(RecipientDTO recipientDTO) throws SQLException {
    String selectMemberId = "SELECT m.id_member "
        + "FROM project_pae.members m "
        + "WHERE m.username = ?";
    String query = "INSERT INTO project_pae.recipients (id_item, id_member, received) "
        + "VALUES (?, (" + selectMemberId + "), '" + RECEIVED_DEFAULT + "');";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, recipientDTO.getItem().getId());
      ps.setString(2, StringEscapeUtils.escapeHtml4(
          recipientDTO.getMember().getUsername()
      ));
      return ps.executeUpdate() != 0;
    }
  }
}
