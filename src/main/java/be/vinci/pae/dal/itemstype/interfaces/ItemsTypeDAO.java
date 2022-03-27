package be.vinci.pae.dal.itemstype.interfaces;

import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import java.util.List;

public interface ItemsTypeDAO {

  List<ItemsTypeDTO> getAll();
}
