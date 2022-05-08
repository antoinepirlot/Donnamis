package be.vinci.pae.biz.itemstype.interfaces;

import java.util.List;

public interface ItemsTypeUCC {

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
   */
  void addItemsType(ItemsTypeDTO itemsTypeDTO);
}
