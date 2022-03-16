package be.vinci.pae.dal;

import be.vinci.pae.biz.ItemDTO;
import java.util.List;

public interface ItemDAO {

  List<ItemDTO> getLatestItems();

  List<ItemDTO> getAllItems();

  ItemDTO getOneItem(int id);

  ItemDTO cancelOffer(int id);
}
