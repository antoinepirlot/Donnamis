package be.vinci.pae.biz.item.interfaces;

import java.util.List;

public interface ItemUCC {

  List<ItemDTO> getLatestItems();

  List<ItemDTO> getAllItems();

  /**
   * Ask itemDAO to give a list of all offered items.
   * @return a list of all offered items
   */
  List<ItemDTO> getAllOfferedItems();

  ItemDTO getOneItem(int id);

  boolean addItem(ItemDTO itemDTO);

  ItemDTO cancelOffer(int id);
}
