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

public class ItemDAOImpl implements ItemDAO {


  private static final String DEFAULT_OFFER_STATUS = "donated";
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
      ps.setString(1, itemDTO.getItemDescription());
      ps.setString(2, itemDTO.getItemType().getItemType());
      ps.setInt(3, itemDTO.getMember().getId());
      ps.setString(4, itemDTO.getPhoto());
      ps.setString(5, itemDTO.getTitle());
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
  public ItemDTO modifyItem(ItemDTO itemDTO) throws SQLException {
    String selectIdTypeQuery = "SELECT id_type "
        + "FROM project_pae.items_types "
        + "WHERE item_type = ? ";
    String query = "UPDATE project_pae.items SET item_description = ?, photo = ?, title = ?, "
        + "id_type = (" + selectIdTypeQuery + ") WHERE id_item = ? RETURNING *";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setString(1, itemDTO.getItemDescription());
      preparedStatement.setString(2, itemDTO.getPhoto());
      preparedStatement.setString(3, itemDTO.getTitle());
      preparedStatement.setString(4, itemDTO.getItemType().getItemType());
      preparedStatement.setInt(5, itemDTO.getId());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return ObjectsInstanceCreator.createItemInstance(this.factory, rs);
        }
      }
    } catch (SQLException e) {
      this.logger.log(Level.SEVERE, e.getMessage());
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
          if (itemDTO != null) {
            itemsDTO.add(itemDTO);
          }
        }
      }
    }
    return itemsDTO;
  }
}
