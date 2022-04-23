package be.vinci.pae.dal.item.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import be.vinci.pae.ihm.logs.LoggerHandler;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.text.StringEscapeUtils;

public class ItemDAOImpl implements ItemDAO {


  private static final String DEFAULT_OFFER_STATUS = "donated";
  private static final String GIVEN_OFFER_STATUS = "given";
  private static final String WAITING_RECIPIENT_STATUS = "waiting";
  private static final String RECEIVED_RECIPIENT_STATUS = "received";
  private static final String NOT_RECEIVED_RECIPIENT_STATUS = "not_received";
  private final Logger logger = LoggerHandler.getLogger();
  @Inject
  private Factory factory;
  @Inject
  private DALBackendService dalBackendService;

  @Override
  public List<ItemDTO> getAllItems(String offerStatus) throws SQLException {
    List<ItemDTO> itemsDTOList = new ArrayList<>();
    String query = "SELECT i.id_item, "
        + "                i.item_description, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.offer_status, "
        + "                i.last_offer_date, "
        + "                it.id_type, "
        + "                it.item_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name "
        + "FROM project_pae.items i, "
        + "     project_pae.items_types it, "
        + "     project_pae.members m "
        + "WHERE i.id_type = it.id_type "
        + "  AND i.id_member = m.id_member ";
    if (offerStatus != null) {
      query += "AND i.offer_status = ?;";
    }
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      if (offerStatus != null) {
        ps.setString(1, offerStatus);
      }
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          itemsDTOList.add(ObjectsInstanceCreator.createItemInstance(this.factory, rs));
        }
      }
    }
    return itemsDTOList;
  }

  @Override
  public ItemDTO getOneItem(int id) throws SQLException {
    String query = ""
        + "SELECT i.id_item, i.item_description, i.photo, i.title, i.offer_status, "
        + "       i.last_offer_date, "
        + "       it.id_type, it.item_type, i.last_offer_date, "
        + "       m.id_member, m.username, m.last_name, m.first_name "
        + "FROM project_pae.items i, "
        + "     project_pae.items_types it, "
        + "     project_pae.members m "
        + "WHERE i.id_item = ? "
        + "  AND i.id_type = it.id_type "
        + "  AND i.id_member = m.id_member;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return ObjectsInstanceCreator.createItemInstance(factory, rs);
        }
      }
    }
    return null;
  }

  @Override
  public int addItem(ItemDTO itemDTO) throws SQLException {
    String selectIdTypeQuery = "SELECT id_type "
        + "FROM project_pae.items_types "
        + "WHERE item_type = ? ";
    String query =
        "INSERT INTO project_pae.items (item_description, id_type, id_member, photo, "
            + "title, offer_status, last_offer_date) "
            + "VALUES (?, (" + selectIdTypeQuery + "), ?, ?, ?, ?, ? ) "
            + "RETURNING id_item;";
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      //Select query
      ps.setString(1, StringEscapeUtils.escapeHtml4(itemDTO.getItemDescription()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(
          itemDTO.getItemType().getItemType()
      ));
      ps.setInt(3, itemDTO.getMember().getId());
      ps.setString(4, StringEscapeUtils.escapeHtml4(itemDTO.getPhoto()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(itemDTO.getTitle()));
      ps.setString(6, DEFAULT_OFFER_STATUS);
      ps.setTimestamp(7, itemDTO.getLastOfferDate());
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          this.logger.log(Level.INFO, "Item correctly added");
          return rs.getInt("id_item");
        }
      }
    }
    return -1;
  }

  @Override
  public ItemDTO cancelItem(int id) throws SQLException {
    String query = "UPDATE project_pae.items "
        + "SET offer_status = 'cancelled' "
        + "WHERE id_item = ? "
        + "RETURNING *";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          this.logger.log(Level.INFO, "Item correctly cancelled");
          return ObjectsInstanceCreator.createItemInstance(factory, rs);
        }
      }
    }
    return null;
  }

  @Override
  public List<ItemDTO> getAllItemsOfAMember(int idMember) throws SQLException {
    List<ItemDTO> itemsDTO = new ArrayList<>();
    String query = "SELECT i.id_item, "
        + "                i.item_description, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.offer_status, "
        + "                i.last_offer_date, "
        + "                it.id_type, "
        + "                it.item_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name "
        + "FROM project_pae.items i, "
        + "     project_pae.items_types it, "
        + "     project_pae.members m "
        + "WHERE i.id_type = it.id_type "
        + "  AND i.id_member = m.id_member "
        + "  AND m.id_member = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ItemDTO itemDTO = ObjectsInstanceCreator.createItemInstance(this.factory, rs);
          itemsDTO.add(itemDTO);
        }
      }
    }
    return itemsDTO;
  }

  @Override
  public List<ItemDTO> getAssignedItems(int idMember) throws SQLException {
    String query = "SELECT DISTINCT i.id_item, "
        + "                i.item_description, "
        + "                i.id_type, "
        + "                i.id_member, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.offer_status, "
        + "                i.last_offer_date, "
        + "                it.id_type, "
        + "                it.item_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name "
        + "FROM project_pae.items i, "
        + "     project_pae.members m, "
        + "     project_pae.items_types it, "
        + "     project_pae.recipients r "
        + "WHERE i.id_type = it.id_type "
        + "  AND r.id_member = m.id_member "
        + "  AND r.id_item = i.id_item "
        + "  AND r.received = '" + WAITING_RECIPIENT_STATUS + "' "
        + "  AND m.id_member = ?;";
    List<ItemDTO> listItemDTO = new ArrayList<>();
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          listItemDTO.add(ObjectsInstanceCreator.createItemInstance(this.factory, rs));
        }
      }
    }
    return listItemDTO;
  }

  @Override
  public boolean markItemAsGiven(ItemDTO itemDTO) throws SQLException {
    return this.markItemAs(true, itemDTO);
  }

  @Override
  public boolean markItemAsNotGiven(ItemDTO itemDTO) throws SQLException {
    return this.markItemAs(false, itemDTO);
  }

  @Override
  public int countNumberOfItemsByOfferStatus(int idMember, String offerStatus) throws SQLException {
    String query = "SELECT COUNT(id_item) "
        + "FROM project_pae.items "
        + "WHERE id_member = ? "
        + "  AND offer_status = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      ps.setString(2, StringEscapeUtils.escapeHtml4(offerStatus));
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getInt(1);
      }
    }
  }

  @Override
  public int countNumberOfReceivedOrNotReceivedItems(int idMember, boolean received)
      throws SQLException {
    String query = "SELECT COUNT(DISTINCT id_item) "
        + "FROM     project_pae.recipients "
        + "WHERE id_member = ? "
        + "  AND received = ";
    if(received) {
      query += "'"+RECEIVED_RECIPIENT_STATUS+"';";
    } else {
      query += "'"+NOT_RECEIVED_RECIPIENT_STATUS+"';";
    }
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        return rs.getInt(1);
      }
    }
  }

  @Override
  public List<ItemDTO> getMemberItemsByOfferStatus(int idMember, String offerStatus)
      throws SQLException {
    String query = "SELECT i.id_item, "
        + "                i.item_description, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.offer_status, "
        + "                i.last_offer_date, "
        + "                it.id_type, "
        + "                it.item_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name "
        + "FROM project_pae.items i, "
        + "     project_pae.items_types it, "
        + "     project_pae.members m "
        + "WHERE i.id_type = it.id_type "
        + "  AND i.id_member = m.id_member "
        + "  AND m.id_member = ? ";
    if (offerStatus != null && !offerStatus.isBlank()) {
      query += "AND i.offer_status = ?";
    }
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      if (offerStatus != null && !offerStatus.isBlank()) {
        ps.setString(2, StringEscapeUtils.escapeHtml4(offerStatus));
      }
      try (ResultSet rs = ps.executeQuery()) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        while (rs.next()) {
          itemDTOList.add(ObjectsInstanceCreator.createItemInstance(this.factory, rs));
        }
        return itemDTOList.isEmpty() ? null : itemDTOList;
      }
    }
  }

  @Override
  public List<ItemDTO> getMemberReceivedItems(int idMember) throws SQLException {
    String query = "SELECT i.id_item, "
        + "       i.item_description, "
        + "       i.photo, "
        + "       i.title, "
        + "       i.offer_status, "
        + "       i.last_offer_date, "
        + "       it.id_type, "
        + "       it.item_type "
        + "FROM project_pae.items i, "
        + "     project_pae.items_types it, "
        + "     project_pae.recipients r "
        + "WHERE r.received = '"+RECEIVED_RECIPIENT_STATUS+"' "
        + "  AND r.id_member = ? "
        + "  AND r.id_item = i.id_item "
        + "  AND i.id_type = it.id_type;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      try (ResultSet rs = ps.executeQuery()) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        while (rs.next()) {
          itemDTOList.add(ObjectsInstanceCreator.createItemInstance(this.factory, rs));
        }
        return itemDTOList.isEmpty() ? null : itemDTOList;
      }
    }
  }

  /////////////////////////////////////////////////////////
  ///////////////////////UTILS/////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * mark item as given or not given and update the recipient.
   *
   * @param given   true if it marks item as given or false to mark item as not given
   * @param itemDTO the item to update
   * @return true if the operation worked as expected otherwise false
   * @throws SQLException if an error occurs while updating items or recipient
   */
  private boolean markItemAs(boolean given, ItemDTO itemDTO) throws SQLException {
    String selectIdMember = "SELECT DISTINCT r.id_member "
        + "FROM project_pae.items i, "
        + "     project_pae.recipients r "
        + "WHERE r.id_item = i.id_item "
        + "  AND r.received = ? "
        + "  AND i.id_member = ?";
    String query = "UPDATE project_pae.items "
        + "SET offer_status = ? "
        + "WHERE id_item = ?; "
        + "UPDATE project_pae.recipients "
        + "SET received = ? "
        + "WHERE id_member = (" + selectIdMember + ") "
        + "  AND id_item = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      if (given) {
        ps.setString(1, GIVEN_OFFER_STATUS);
        ps.setString(3, RECEIVED_RECIPIENT_STATUS);
      } else {
        ps.setString(1, DEFAULT_OFFER_STATUS);
        ps.setString(3, NOT_RECEIVED_RECIPIENT_STATUS);
      }
      ps.setInt(2, itemDTO.getId());
      ps.setString(4, WAITING_RECIPIENT_STATUS);
      ps.setInt(5, itemDTO.getMember().getId());
      ps.setInt(6, itemDTO.getId());
      return ps.executeUpdate() != 0;
    }
  }
}
