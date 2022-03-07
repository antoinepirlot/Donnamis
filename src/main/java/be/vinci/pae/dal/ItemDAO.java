package be.vinci.pae.dal;

import be.vinci.pae.biz.ItemDTO;
import java.util.List;

public interface ItemDAO {

  List<ItemDTO> getLatestItems();
}
