package be.vinci.pae.dal.interfaces.item;

import be.vinci.pae.biz.interfaces.item.ItemDTO;
import java.util.List;

public interface ItemDAO {

  List<ItemDTO> getLatestItems();

  List<ItemDTO> getAllItems();

  ItemDTO getOneItem(int id);

  boolean addItem(ItemDTO itemDTO);

  ItemDTO cancelOffer(int id);
}
