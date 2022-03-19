package be.vinci.pae.dal.objects.item;

import be.vinci.pae.biz.factory.Factory;
import be.vinci.pae.biz.interfaces.item.ItemDTO;
import be.vinci.pae.biz.interfaces.item.items_type.ItemTypeDTO;
import be.vinci.pae.dal.interfaces.item.ItemDAO;
import be.vinci.pae.dal.interfaces.member.MemberDAO;
import be.vinci.pae.dal.services.DALServices;
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
  public ItemDTO cancelOffer(int id) {
    String query = "UPDATE project_pae.items SET offer_status = 'cancelled' WHERE id_item = ? "
        + "RETURNING *";
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          ItemDTO itemDTO = createItemInstance(rs);
          return itemDTO;
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
