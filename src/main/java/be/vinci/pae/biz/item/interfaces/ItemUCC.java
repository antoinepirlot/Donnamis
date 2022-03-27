package be.vinci.pae.biz.item.interfaces;

import java.util.List;

public interface ItemUCC {

  /**
   * Ask itemDAO to give a list of the latest items.
   *
   * @return a list of the latest items
   */
  List<ItemDTO> getLatestItems();

  /**
   * Ask itemDAO to give a list of all items.
   *
   * @return a list of all items
   */
  List<ItemDTO> getAllItems();

  /**
   * Ask itemDAO to give a list of all offered items.
   *
   * @return a list of all offered items
   */
  List<ItemDTO> getAllOfferedItems();

  /**
   * Ask itemDAO to get one item identified by its id.
   *
   * @return the item associated by the id
   */
  ItemDTO getOneItem(int id);

  /**
   * Ask itemDAO to add the item.
   *
   * @return true if the adding is a success, otherwise false
   */
  boolean addItem(ItemDTO itemDTO);

  /**
   * Ask itemDAO to cancel the item identified by its id.
   *
   * @return the cancelled item
   */
  ItemDTO cancelOffer(int id);

  /**
   * Ask itemDAO to get all the items identified by the member id.
   *
   * @return all the member's items
   */
  List<ItemDTO> getAllItemsByMemberId(int id);
}
