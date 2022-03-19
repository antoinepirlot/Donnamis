package be.vinci.pae.biz.item.interfaces;

import java.util.List;

public interface ItemUCC {

  List<ItemDTO> getLatestItems();

  List<ItemDTO> getAllItems();

  ItemDTO getOneItem(int id);

  boolean addItem(ItemDTO itemDTO);

  ItemDTO cancelOffer(int id);
}
