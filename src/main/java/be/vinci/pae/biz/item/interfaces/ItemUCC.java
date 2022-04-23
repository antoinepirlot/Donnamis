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
   * Ask itemDAO to get all the items identified by the member id.
   *
   * @return all the member's items
   */
  List<ItemDTO> getAllItemsOfAMember(int idMember) throws SQLException;

  /**
   * This method asks DAO to get assigned items of the member identified by its id.
   *
   * @param idMember the member's id
   * @return the list of assigned items
   * @throws SQLException if an error occurs in the DAO method
   */
  List<ItemDTO> getAssignedItems(int idMember) throws SQLException;

  /**
   * Mark item,identified by its id, as given.
   * @param itemDTO the item to update
   * @return true if the operation worked as expected otherwise false
   * @throws SQLException if an error occurs while updating item
   */
  boolean markItemAsGiven(ItemDTO itemDTO) throws SQLException;

  /**
   * Mark item, identified by its id, as donated and update recipient to not received.
   * @param itemDTO the item to update
   * @return true if the operation worked as expected otherwise false
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
  int countNumberOfReceivedOrNotReceivedItems(int idMember, boolean received) throws SQLException;

  /**
   * Get all donated items of the member identified by its id.
   * @param idMember the member's id
   * @param offerStatus the item's offer status
   * @return the list of member's donated items
   * @throws SQLException if an error occurs while getting items
   */
  List<ItemDTO> getMemberItemsByOfferStatus(int idMember, String offerStatus) throws SQLException;

  /**
   * Get items that the member received.
   * @param idMember the member's id
   * @return the list of member's received items
   * @throws SQLException if an error occurs while getting items
   */
  List<ItemDTO> getMemberReceivedItems(int idMember) throws SQLException;
}
