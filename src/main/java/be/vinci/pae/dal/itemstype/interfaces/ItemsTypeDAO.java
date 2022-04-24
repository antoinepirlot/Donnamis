package be.vinci.pae.dal.itemstype.interfaces;

import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import java.sql.SQLException;
import java.util.List;

public interface ItemsTypeDAO {

  List<ItemsTypeDTO> getAll();

  /**
   * Checks if the items type already exists in the database.
   * @param itemsTypeDTO the itemsType to check
   * @return true if the itemsType already exists otherwise false
   * @throws SQLException if an error occurs while checking the existence of itemsType
   */
  boolean exists(ItemsTypeDTO itemsTypeDTO) throws SQLException;

  /**
   * Add a new items type into the database.
   * @param itemsTypeDTO the items type to add
   * @return true if the itemsTypeDTO has been added false otherwise
   * @throws SQLException if an error occurs while adding the itemsTypeDTO
   */
  boolean addItemsType(ItemsTypeDTO itemsTypeDTO) throws SQLException;
}
