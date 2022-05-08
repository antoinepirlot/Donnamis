package be.vinci.pae.dal.item.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class ItemDAOImpl implements ItemDAO {


  private static final String DEFAULT_OFFER_STATUS = "donated";
  private static final String ASSIGNED_OFFER_STATUS = "assigned";
  private static final String GIVEN_OFFER_STATUS = "given";
  private static final String WAITING_RECIPIENT_STATUS = "waiting";
  private static final String RECEIVED_RECIPIENT_STATUS = "received";
  private static final String NOT_RECEIVED_RECIPIENT_STATUS = "not_received";
  @Inject
  private Factory factory;
  @Inject
  private DALBackendService dalBackendService;

  @Override
  public List<ItemDTO> getAllItems(String offerStatus) {
    List<ItemDTO> itemsDTOList = new ArrayList<>();
    String query = "SELECT i.id_item, "
        + "                i.item_description, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.version_item, "
        + "                i.offer_status, "
        + "                i.last_offer_date, "
        + "                it.id_type, "
        + "                it.item_type,"
        + "                it.version_items_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name, "
        + "                m.version_member "
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
        return itemsDTOList.isEmpty() ? null : itemsDTOList;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public ItemDTO getOneItem(int id) {
    String query = ""
        + "SELECT i.id_item, i.item_description, i.photo, i.title, i.offer_status, "
        + "       i.last_offer_date, "
        + "       it.id_type, it.item_type, it.version_items_type, "
        + "       i.last_offer_date, i.version_item, "
        + "       m.id_member, m.username, m.last_name, m.first_name, m.version_member "
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
        return null;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public int addItem(ItemDTO itemDTO) {
    String selectIdTypeQuery = "SELECT id_type "
        + "FROM project_pae.items_types "
        + "WHERE item_type = ? ";
    String query =
        "INSERT INTO project_pae.items (item_description, id_type, id_member, photo, "
            + "title, offer_status, last_offer_date, version_item) "
            + "VALUES (?, (" + selectIdTypeQuery + "), ?, ?, ?, ?, ?, 1) "
            + "RETURNING id_item;";
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      //Select query
      ps.setString(1, StringEscapeUtils.escapeHtml4(itemDTO.getItemDescription()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(
          itemDTO.getItemType().getItemType()
      ));
      ps.setInt(3, itemDTO.getMember().getId());
      ps.setString(4, itemDTO.getPhoto());
      ps.setString(5, StringEscapeUtils.escapeHtml4(itemDTO.getTitle()));
      ps.setString(6, DEFAULT_OFFER_STATUS);
      ps.setTimestamp(7, itemDTO.getLastOffer().getDate());
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getInt("id_item") : -1;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public ItemDTO cancelItem(int id) {
    String query = "UPDATE project_pae.items "
        + "SET offer_status = 'cancelled', version_item = version_item + 1 "
        + "WHERE id_item = ? "
        + "RETURNING *";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return ObjectsInstanceCreator.createItemInstance(factory, rs);
        }
        return null;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean modifyItem(ItemDTO itemDTO) {
    String selectLastIdOffer = "(SELECT id_offer "
        + "FROM project_pae.offers "
        + "WHERE id_item = ? "
        + "ORDER BY date DESC "
        + "LIMIT 1)";
    String query = "UPDATE project_pae.items SET item_description = ?, ";
    if (itemDTO.getPhoto() != null && !itemDTO.getPhoto().isBlank()) {
      query += "photo = ?, ";
    }
    query += "version_item = version_item + 1 "
        + "WHERE id_item = ?; "
        + "UPDATE project_pae.offers "
        + "SET time_slot = ?, version_offer = version_offer + 1 "
        + "WHERE id_item = " + selectLastIdOffer + ";";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setString(1, itemDTO.getItemDescription());
      if (itemDTO.getPhoto() == null || itemDTO.getPhoto().isBlank()) {
        preparedStatement.setInt(2, itemDTO.getId());
        preparedStatement.setString(3, StringEscapeUtils
            .escapeHtml4(itemDTO.getLastOffer().getTimeSlot()));
        preparedStatement.setInt(4, itemDTO.getId());
      } else {
        preparedStatement.setString(2, itemDTO.getPhoto());
        preparedStatement.setInt(3, itemDTO.getId());
        preparedStatement.setString(4, StringEscapeUtils
            .escapeHtml4(itemDTO.getLastOffer().getTimeSlot()));
        preparedStatement.setInt(5, itemDTO.getId());
      }
      return preparedStatement.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getAllItemsOfAMember(int idMember) {
    List<ItemDTO> itemsDTO = new ArrayList<>();
    String query = "SELECT i.id_item, "
        + "                i.item_description, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.offer_status, "
        + "                i.last_offer_date,"
        + "                i.version_item, "
        + "                it.id_type, "
        + "                it.item_type,"
        + "                it.version_items_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name, "
        + "                m.version_member "
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
        return itemsDTO.isEmpty() ? null : itemsDTO;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getAssignedItems(int idMember) {
    String query = "SELECT DISTINCT i.id_item, "
        + "                i.item_description, "
        + "                i.id_type, "
        + "                i.id_member, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.offer_status, "
        + "                i.last_offer_date,"
        + "                i.version_item, "
        + "                it.id_type, "
        + "                it.item_type,"
        + "                it.version_items_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name,"
        + "                m.version_member "
        + "FROM project_pae.items i, "
        + "     project_pae.members m, "
        + "     project_pae.items_types it, "
        + "     project_pae.recipients r "
        + "WHERE i.id_type = it.id_type "
        + "  AND r.id_member = m.id_member "
        + "  AND r.id_item = i.id_item "
        + "  AND m.id_member = ?;";
    return getItemDTOList(idMember, query);
  }

  @Override
  public List<ItemDTO> getGivenItems(int idMember) {
    String query = "SELECT DISTINCT i.id_item, i.item_description, i.id_type, i.id_member, "
        + "i.photo, i.title, i.offer_status, i.last_offer_date, i.version_item, it.id_type, "
        + "it.item_type, it.version_items_type, m.id_member, m.username, m.last_name, "
        + "m.first_name, m.version_member "
        + "FROM project_pae.items i, project_pae.members m, project_pae.items_types it "
        + "WHERE i.id_type = it.id_type AND i.id_member = m.id_member AND "
        + "m.id_member = ? AND i.offer_status = 'given';";
    return getItemDTOList(idMember, query);
  }

  @Override
  public boolean markItemAsGiven(ItemDTO itemDTO) {
    try {
      return this.markItemAs(true, itemDTO);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean markItemAsNotGiven(ItemDTO itemDTO) {
    try {
      return this.markItemAs(false, itemDTO);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public int countNumberOfItemsByOfferStatus(int idMember, String offerStatus) {
    String query = "SELECT COUNT(id_item) "
        + "FROM project_pae.items "
        + "WHERE id_member = ? "
        + "  AND offer_status = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      ps.setString(2, StringEscapeUtils.escapeHtml4(offerStatus));
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getInt(1) : -1;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public int countNumberOfReceivedOrNotReceivedItems(int idMember, boolean received) {
    String query = "SELECT COUNT(DISTINCT id_item) "
        + "FROM     project_pae.recipients "
        + "WHERE id_member = ? "
        + "  AND received = ";
    if (received) {
      query += "'" + RECEIVED_RECIPIENT_STATUS + "';";
    } else {
      query += "'" + NOT_RECEIVED_RECIPIENT_STATUS + "';";
    }
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getInt(1) : -1;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getMemberItemsByOfferStatus(int idMember, String offerStatus) {
    String query = "SELECT i.id_item, "
        + "                i.item_description, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.offer_status, "
        + "                i.last_offer_date,"
        + "                i.version_item, "
        + "                it.id_type, "
        + "                it.item_type,"
        + "                it.version_items_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name, "
        + "                m.version_member "
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
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getMemberReceivedItems(int idMember) {
    String query = "SELECT i.id_item, "
        + "       i.item_description, "
        + "       i.photo, "
        + "       i.title, "
        + "       i.offer_status, "
        + "       i.last_offer_date, "
        + "       i.version_item, "
        + "       it.id_type, "
        + "       it.item_type,"
        + "       it.version_items_type "
        + "FROM project_pae.items i, "
        + "     project_pae.items_types it, "
        + "     project_pae.recipients r "
        + "WHERE r.received = '" + RECEIVED_RECIPIENT_STATUS + "' "
        + "  AND r.id_member = ? "
        + "  AND r.id_item = i.id_item "
        + "  AND i.id_type = it.id_type;";
    return getItemDTOList(idMember, query);
  }

  @Override
  public boolean addPhoto(int idItem, String photoName) {
    String query = "UPDATE project_pae.items "
        + "SET photo = ?, version_item = version_item + 1 "
        + "WHERE id_item = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, photoName);
      ps.setInt(2, idItem);
      return ps.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getAllPublicItems() {
    String query = "SELECT i.id_item, "
        + "                i.item_description, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.version_item, "
        + "                i.offer_status, "
        + "                i.last_offer_date, "
        + "                it.id_type, "
        + "                it.item_type,"
        + "                it.version_items_type, "
        + "                m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name, "
        + "                m.version_member "
        + "FROM project_pae.items i, "
        + "     project_pae.items_types it, "
        + "     project_pae.members m "
        + "WHERE i.id_type = it.id_type "
        + "  AND i.id_member = m.id_member "
        + "  AND (i.offer_status = '" + DEFAULT_OFFER_STATUS + "' "
        + "       OR i.offer_status = '" + ASSIGNED_OFFER_STATUS + "'"
        + "  );";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      try (ResultSet rs = ps.executeQuery()) {
        List<ItemDTO> itemsDTOList = new ArrayList<>();
        while (rs.next()) {
          itemsDTOList.add(ObjectsInstanceCreator.createItemInstance(this.factory, rs));
        }
        return itemsDTOList.isEmpty() ? null : itemsDTOList;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean itemExists(int idItem) {
    String query = "SELECT id_item "
        + "FROM project_pae.items "
        + "WHERE id_item = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idItem);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
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
        + "  AND i.id_item = ?";
    String query = "UPDATE project_pae.items "
        + "SET offer_status = ?, version_item = version_item + 1 "
        + "WHERE id_item = ?; "
        + "UPDATE project_pae.recipients "
        + "SET received = ?, version_recipient = version_recipient + 1 "
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

  /**
   * Set id member to the query and create an item dto list.
   *
   * @param idMember the member's id to set into the query
   * @param query    the query of the sql request
   * @return the list of items
   */
  private List<ItemDTO> getItemDTOList(int idMember, String query) {
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      try (ResultSet rs = ps.executeQuery()) {
        List<ItemDTO> listItemDTO = new ArrayList<>();
        while (rs.next()) {
          listItemDTO.add(ObjectsInstanceCreator.createItemInstance(this.factory, rs));
        }
        return listItemDTO.isEmpty() ? null : listItemDTO;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

}
