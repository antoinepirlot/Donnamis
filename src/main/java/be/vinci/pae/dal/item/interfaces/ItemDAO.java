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

  /**
   * Mark the item, identified by its id, as donated and update recipient to not received.
   * @param itemDTO the item to update
   * @return true if the item has been update otherwise false
   * @throws SQLException if an error occurs while updating item
   */
  boolean markItemAsNotGiven(ItemDTO itemDTO) throws SQLException;

  /**
   * Count the number of items with the specified offer status for the member with the idMember.
   * @param idMember the member's id
   * @param offerStatus the item's offer status
   * @return the number of items matching the offer status
   * @throws SQLException if an error occurs while counting
   */
  int countNumberOfItemsByOfferStatus(int idMember, String offerStatus) throws SQLException;

  /**
   * Count the number of items that have been received or not by the member.
   * If received is true that means the item has been received by the member.
   * If received is false that means the member marked his interest in the item but
   * the member who offers the item marked the member has never received the item.
   * @param idMember the member's id
   * @param received true if the item has been received by the member
   *                 false if the member had marked its interest but never take the item.
   * @return the number of the number of items received or not received
   * @throws SQLException if an error occurs while counting items
   */
  int countNumberOfReceivedOrNotReceivedItems(int idMember, boolean received)throws SQLException;
}
