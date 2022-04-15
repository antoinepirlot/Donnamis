package be.vinci.pae.dal.item.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {


  private static final String DEFAULT_OFFER_STATUS = "donated";
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
  public ItemDTO getOneItem(int id) {
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
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public int addItem(ItemDTO itemDTO) {
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
          System.out.println("Ajout de l'offre réussi.");
          return rs.getInt("id_item");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  @Override
  public ItemDTO cancelOffer(int id) {
    String query = "UPDATE project_pae.items SET offer_status = 'cancelled' WHERE id_item = ? "
        + "RETURNING *";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return ObjectsInstanceCreator.createItemInstance(factory, rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public List<ItemDTO> getAllItemsOfAMember(int idMember) {
    System.out.println("Get all offered items for a Member (ItemDAOImpl)");
    List<ItemDTO> itemsDTO = new ArrayList<>();
    String query = "SELECT i.id_item, i.item_description, i.photo, i.title, i.offer_status, "
        + "i.last_offer_date, it.id_type, it.item_type, "
        + "m.username, m.last_name, m.first_name "
        + "FROM project_pae.items i, project_pae.items_types it, project_pae.members m "
        + "WHERE i.id_type = it.id_type AND i.id_member = m.id_member "
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return itemsDTO;
  }

  @Override
  public boolean updateLatestOfferDate(ItemDTO itemDTO) {
    String query = "UPDATE project_pae.items ON latest_offer_date = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setTimestamp(1, itemDTO.getLastOfferDate());
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }


}
