package be.vinci.pae.dal.item.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import java.util.List;

public interface ItemDAO {

  /**
   * Get all items from the db by status if it's defined or all offer status type if not.
   *
   * @return a list of all offered items
   */
  List<ItemDTO> getAllItems(String offerStatus);

  List<ItemDTO> getAllItemsOfAMember(int id);

  ItemDTO getOneItem(int id);

  int addItem(ItemDTO itemDTO);

  ItemDTO cancelItem(int id);

  /**
   * This method get assigned items of a member from the database.
   *
   * @param idMember the member's id
   * @return the list of assigned items
   */
  List<ItemDTO> getAssignedItems(int idMember);

  /**
   * This method get given items of a member from the database.
   *
   * @param idMember the member's id
   * @return the list of given items
   */
  List<ItemDTO> getGivenItems(int idMember);

  boolean modifyItem(ItemDTO itemDTO);

  /**
   * Mark the item identified by its id as given.
   *
   * @param itemDTO the item to update
   * @return true if the item has been updated otherwise false
   */
  boolean markItemAsGiven(ItemDTO itemDTO);

  /**
   * Mark the item, identified by its id, as donated and update recipient to not received.
   *
   * @param itemDTO the item to update
   * @return true if the item has been update otherwise false
   */
  boolean markItemAsNotGiven(ItemDTO itemDTO);

  /**
   * Count the number of items with the specified offer status for the member with the idMember.
   *
   * @param idMember    the member's id
   * @param offerStatus the item's offer status
   * @return the number of items matching the offer status
   */
  int countNumberOfItemsByOfferStatus(int idMember, String offerStatus);

  /**
   * Count the number of items that have been received or not by the member. If received is true
   * that means the item has been received by the member. If received is false that means the member
   * marked his interest in the item but the member who offers the item marked the member has never
   * received the item.
   *
   * @param idMember the member's id
   * @param received true if the item has been received by the member false if the member had marked
   *                 its interest but never take the item.
   * @return the number of the number of items received or not received
   */
  int countNumberOfReceivedOrNotReceivedItems(int idMember, boolean received);

  /**
   * Get all donated items of the member identified by its id.
   *
   * @param idMember    the member's id
   * @param offerStatus the item's offer status
   * @return the list of member's donated items
   */
  List<ItemDTO> getMemberItemsByOfferStatus(int idMember, String offerStatus);

  /**
   * Get items that the member received.
   *
   * @param idMember the member's id
   * @return the list of member's received items
   */
  List<ItemDTO> getMemberReceivedItems(int idMember);

  /**
   * Add photo name to the database for the idItem.
   *
   * @param idItem    the item's id
   * @param photoName the photo's name
   * @return true if the photo has been added otherwise false
   */
  boolean addPhoto(int idItem, String photoName);

  /**
   * Get all donated and assigned items.
   *
   * @return the list of assigned and donated items
   */
  List<ItemDTO> getAllPublicItems();

  /**
   * Checks if the item exists into the database.
   *
   * @param idItem the item's id
   * @return true if it exists otherwise false
   */
  boolean itemExists(int idItem);
}
