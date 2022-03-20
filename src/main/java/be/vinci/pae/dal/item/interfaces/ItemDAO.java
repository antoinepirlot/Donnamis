package be.vinci.pae.dal.item.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import java.util.List;

public interface ItemDAO {

  List<ItemDTO> getLatestItems();

  List<ItemDTO> getAllItems();

  /**
   * Get all offered items from the db
   * @return a list of all offered items
   */
  List<ItemDTO> getAllOfferedItems();

  ItemDTO getOneItem(int id);

  boolean addItem(ItemDTO itemDTO);

  ItemDTO cancelOffer(int id);
}
