package be.vinci.pae.dal.item.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {

  /**
   * Get all items from the db by status if it's defined or all offer status type if not.
   *
   * @return a list of all offered items
   */
  List<ItemDTO> getAllItems(String offerStatus) throws SQLException;

  List<ItemDTO> getAllItemsOfAMember(int id) throws SQLException;

  ItemDTO getOneItem(int id) throws SQLException;

  int addItem(ItemDTO itemDTO) throws SQLException;

  ItemDTO cancelItem(int id) throws SQLException;

  /**
   * This method get assigned items of a member from the database.
   *
   * @param idMember the member's id
   * @return the list of assigned items of the member
   * @throws SQLException if an error occurs while getting information from the database
   */
  List<ItemDTO> getAssignedItems(int idMember) throws SQLException;

  /**
   * Mark the item identified by its id as given.
   * @param itemDTO the item to update
   * @return true if the item has been updated otherwise false
   * @throws SQLException if an error occurs while updating item
   */
  boolean markItemAsGiven(ItemDTO itemDTO) throws SQLException;
}
