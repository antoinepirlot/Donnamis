package be.vinci.pae.biz.interfaces.item;

import java.util.List;

public interface ItemUCC {

  List<ItemDTO> getLatestItems();

  List<ItemDTO> getAllItems();

  ItemDTO getOneItem(int id);

  ItemDTO cancelOffer(int id);
}
