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

  /**
   * Get the latest items from the database.
   *
   * @return List of the latest items offered
   */
  @Override
  public List<ItemDTO> getLatestItems() {
    System.out.println("getLatestItems");
    List<ItemDTO> itemsToReturn = new ArrayList<>();
    String query = "SELECT items.id_item,\n"
        + "       items.item_description,\n"
        + "       items.id_type,\n"
        + "       items.id_member,\n"
        + "       items.photo,\n"
        + "       items.title,\n"
        + "       items.offer_status\n"
        + "FROM project_pae.items items\n"
        + "         LEFT OUTER JOIN project_pae.offers offers\n"
        + "                         ON items.id_item = offers.id_item\n"
        + "WHERE items.offer_status = 'donated'\n"
        + "ORDER BY offers.date DESC";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      System.out.println("Préparation du statement");
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          ItemDTO itemDTO = ObjectsInstanceCreator.createItemInstance(factory, rs);
          itemsToReturn.add(itemDTO);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Création des objets réussie");

    return itemsToReturn;
  }


  /**
   * Get all items from the database.
   *
   * @return List of all items offered
   */
  @Override
  public List<ItemDTO> getAllItems() {
    System.out.println("getAllItems");
    List<ItemDTO> itemsToReturn = new ArrayList<>();
    try {
      String query = "SELECT * FROM project_pae.items";
      PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          ItemDTO itemDTO = ObjectsInstanceCreator.createItemInstance(factory, rs);
          itemsToReturn.add(itemDTO);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Création des objets réussie");

    return itemsToReturn;
  }

  @Override
  public List<ItemDTO> getAllOfferedItems() {
    System.out.println("Get all offered items (ItemDAOImpl)");
    List<ItemDTO> itemsDTO = new ArrayList<>();
    String query = "SELECT i.id_item, i.item_description, i.photo, i.title, i.offer_status, "
        + "it.id_type, it.item_type, "
        + "m.username, m.last_name, m.first_name "
        + "FROM project_pae.items i, project_pae.items_types it, project_pae.members m "
        + "WHERE i.id_type = it.id_type AND i.id_member = m.id_member AND i.offer_status = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, DEFAULT_OFFER_STATUS);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          itemsDTO.add(ObjectsInstanceCreator.createItemInstance(this.factory, rs));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return itemsDTO;
  }

  @Override
  public ItemDTO getOneItem(int id) {
    String query = ""
        + "SELECT i.id_item, i.item_description, i.photo, i.title, i.offer_status, "
        + "       it.id_type, it.item_type, "
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
  public boolean addItem(ItemDTO itemDTO) {
    String selectIdTypeQuery = "SELECT id_type "
        + "FROM project_pae.items_types "
        + "WHERE item_type = '" + itemDTO.getItemType().getItemType() + "'";
    String query =
        "INSERT INTO project_pae.items (item_description, id_type, id_member, photo, "
            + "title, offer_status) "
            + "VALUES (?, (" + selectIdTypeQuery + "), ?, ?, ?, ?)";
    System.out.println(query);
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, itemDTO.getItemDescription());
      ps.setInt(2, itemDTO.getMember().getId());
      ps.setString(3, itemDTO.getPhoto());
      ps.setString(4, itemDTO.getTitle());
      ps.setString(5, DEFAULT_OFFER_STATUS);
      if (ps.executeUpdate() != 0) {
        System.out.println("Ajout de l'offre réussi.");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
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
  public List<ItemDTO> getAllItemsByMemberId(int id) {
    System.out.println("Get all offered items for a Member (ItemDAOImpl)");
    List<ItemDTO> itemsDTO = new ArrayList<>();
    String query = "SELECT i.id_item, i.item_description, i.photo, i.title, i.offer_status, "
        + "it.id_type, it.item_type, "
        + "m.username, m.last_name, m.first_name "
        + "FROM project_pae.items i, project_pae.items_types it, project_pae.members m "
        + "WHERE i.id_type = it.id_type AND i.id_member = m.id_member AND i.offer_status = ? AND m.id_member = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, DEFAULT_OFFER_STATUS);
      ps.setInt(2, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          itemsDTO.add(ObjectsInstanceCreator.createItemInstance(this.factory, rs));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return itemsDTO;
  }

}
