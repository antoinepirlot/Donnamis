package be.vinci.pae.dal;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.ItemDTO;
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


  private ItemDTO createItemInstance(ResultSet rs) throws SQLException {
    System.out.println("Item instance creation");
    ItemDTO itemDTO = factory.getItem();
    itemDTO.setId(rs.getInt("id_item"));
    itemDTO.setItem_description(rs.getString("item_description"));
    itemDTO.setId_item_type(rs.getInt("id_item_type"));
    itemDTO.setId_member(rs.getInt("id_member"));
    itemDTO.setPhoto(rs.getString("photo"));
    itemDTO.setTitle(rs.getString("title"));
    itemDTO.setOffer_status(rs.getString("offer_status"));
    return itemDTO;
  }

}
