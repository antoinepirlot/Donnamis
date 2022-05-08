package be.vinci.pae.biz.item.interfaces;

import java.util.List;

public interface ItemUCC {

  /**
   * Ask itemDAO to give a list of all items of a defined offer status If offer status is null it
   * returns all items if offer status is not null then it returns all items with this offer
   * status.
   *
   * @return a list of all items
   */
  List<ItemDTO> getAllItems(String offerStatus);

  /**
   * Ask itemDAO to get one item identified by its id.
   *
   * @param itemDTO the itemDTO that contains the id or null
   * @param idItem  the item's id if itemDTO is null
   * @return the item associated by the id
   */
  ItemDTO getOneItem(ItemDTO itemDTO, int idItem);

  /**
   * Ask itemDAO to add the item.
   *
   * @return the item id or -1 if it failed
   */
  int addItem(ItemDTO itemDTO);

  /**
   * Ask itemDAO to cancel the item identified by its id.
   *
   * @return the cancelled item
   */
  ItemDTO cancelItem(int id);

  /**
   * Modify the item.
   *
   * @param itemDTO the new item
   */
  void modifyItem(ItemDTO itemDTO);

  /**
   * Ask itemDAO to get all the items identified by the member id.
   *
   * @return all the member's items
   */
  List<ItemDTO> getAllItemsOfAMember(int idMember);

  /**
   * This method asks DAO to get assigned items of the member identified by its id.
   *
   * @param idMember the member's id
   * @return the list of assigned items
   */
  List<ItemDTO> getAssignedItems(int idMember);

  /**
   * This method asks DAO to get given items of the member identified by its id.
   *
   * @param idMember the member's id
   * @return the list of given items
   */
  List<ItemDTO> getGivenItems(int idMember);

  /**
   * Mark item,identified by its id, as given.
   *
   * @param itemDTO the item to update
   */
  void markItemAsGiven(ItemDTO itemDTO);

  /**
   * Mark item, identified by its id, as donated and update recipient to not received.
   *
   * @param itemDTO the item to update
   */
  void markItemAsNotGiven(ItemDTO itemDTO);

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
   */
  void addPhoto(int idItem, String photoName);

  /**
   * Checks if the item exists into the database.
   *
   * @param idItem the item's id
   * @return true if it exists otherwise false
   */
  boolean itemExists(int idItem);

  /**
   * Get all donated and assigned items.
   *
   * @return the list of assigned and donated items
   */
  List<ItemDTO> getAllPublicItems();
}
