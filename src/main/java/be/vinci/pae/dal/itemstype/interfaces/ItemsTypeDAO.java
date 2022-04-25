package be.vinci.pae.dal.itemstype.interfaces;

import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import java.util.List;

public interface ItemsTypeDAO {

  List<ItemsTypeDTO> getAll();

  /**
   * Checks if the items type already exists in the database.
   *
   * @param itemsTypeDTO the itemsType to check
   * @return true if the itemsType already exists otherwise false
   */
  boolean exists(ItemsTypeDTO itemsTypeDTO);

  /**
   * Add a new items type into the database.
   *
   * @param itemsTypeDTO the items type to add
   * @return true if the itemsTypeDTO has been added false otherwise
   */
  boolean addItemsType(ItemsTypeDTO itemsTypeDTO);
}
