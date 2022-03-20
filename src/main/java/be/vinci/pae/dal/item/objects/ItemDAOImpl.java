package be.vinci.pae.dal.item.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.itemsType.interfaces.ItemTypeDTO;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {


  @Inject
  private Factory factory;
  @Inject
  private DALServices dalServices;
  @Inject
  private MemberDAO memberDAO;
  private static final String DEFAULT_OFFER_STATUS = "donated";

  /**
   * Get the latest items from the database.
   *
   * @return List of the latest items offered
   */
  @Override
  public List<ItemDTO> getLatestItems() {
    System.out.println("getLatestItems");
    List<ItemDTO> itemsToReturn = new ArrayList<>();
    try {
      String query = "SELECT items.id_item,\n"
          + "       items.item_description,\n"
          + "       items.id_item_type,\n"
          + "       items.id_member,\n"
          + "       items.photo,\n"
          + "       items.title,\n"
          + "       items.offer_status\n"
          + "FROM project_pae.items items\n"
          + "         LEFT OUTER JOIN project_pae.offers offers\n"
          + "                         ON items.id_item = offers.id_item\n"
          + "ORDER BY offers.date DESC";
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          ItemDTO itemDTO = createItemInstance(rs);
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
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          ItemDTO itemDTO = createItemInstance(rs);
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
        + "WHERE i.id_item_type = it.id_type AND i.id_member = m.id_member AND i.offer_status = ?;";
    try (PreparedStatement ps = this.dalServices.getPreparedStatement(query)) {
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
    String query = "SELECT * FROM project_pae.items i WHERE i.id_item = ?";
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return createItemInstance(rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public boolean addItem(ItemDTO itemDTO) {
    String query =
        "INSERT INTO project_pae.items (item_description, id_item_type, id_member, photo, "
            + "title, offer_status) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = dalServices.getPreparedStatement(query)) {
      ps.setString(1, itemDTO.getItemDescription());
      ps.setInt(2, itemDTO.getItemType().getIdType());
      ps.setInt(3, itemDTO.getMember().getId());
      ps.setString(4, itemDTO.getPhoto());
      ps.setString(5, itemDTO.getTitle());
      ps.setString(6, DEFAULT_OFFER_STATUS);
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
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return createItemInstance(rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }


  private ItemDTO createItemInstance(ResultSet rs) throws SQLException {
    ItemDTO itemDTO = factory.getItem();
    itemDTO.setId(rs.getInt("id_item"));
    itemDTO.setItemDescription(rs.getString("item_description"));
    itemDTO.setItemType(createItemTypeInstance(rs));
    itemDTO.setMember(memberDAO.getOneMember(rs.getInt("id_member")));
    itemDTO.setPhoto(rs.getString("photo"));
    itemDTO.setTitle(rs.getString("title"));
    itemDTO.setOfferStatus(rs.getString("offer_status"));
    return itemDTO;
  }

  private ItemTypeDTO createItemTypeInstance(ResultSet rs) throws SQLException {
    ItemTypeDTO itemTypeDTO = factory.getItemType();
    itemTypeDTO.setIdType(rs.getInt("id_item_type"));
    itemTypeDTO.setItemType(rs.getString("item_type"));
    return itemTypeDTO;
  }

}
