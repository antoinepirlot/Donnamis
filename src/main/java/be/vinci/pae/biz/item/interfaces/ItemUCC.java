package be.vinci.pae.biz.item.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ItemUCC {

  /**
   * Ask itemDAO to give a list of all items of a defined offer status If offer status is null it
   * returns all items if offer status is not null then it returns all items with this offer
   * status.
   *
   * @return a list of all items
   */
  List<ItemDTO> getAllItems(String offerStatus) throws SQLException;

  /**
   * Ask itemDAO to get one item identified by its id.
   *
   * @return the item associated by the id
   */
  ItemDTO getOneItem(int id) throws SQLException;

  /**
   * Ask itemDAO to add the item.
   *
   * @return the item id or -1 if it failed
   */
  int addItem(ItemDTO itemDTO) throws SQLException;

  /**
   * Ask itemDAO to cancel the item identified by its id.
   *
   * @return the cancelled item
   */
  ItemDTO cancelItem(int id) throws SQLException;

  /**
   * Modify the item.
   *
   * @param itemDTO the new item
   * @return the modify item
   * @throws SQLException SQL error
   */
  ItemDTO modifyItem(ItemDTO itemDTO) throws SQLException;

  /**
   * Ask itemDAO to get all the items identified by the member id.
   *
   * @return all the member's items
   */
  List<ItemDTO> getAllItemsOfAMember(int idMember) throws SQLException;
}
