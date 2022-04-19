package be.vinci.pae.dal.item.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {

  /**
   * Get all items from the db by status if it's defined or all offer status type if not.
   *
   * @return a list of all offered items
   */
  List<ItemDTO> getAllItems(String offerStatus) throws SQLException;

  List<ItemDTO> getAllItemsOfAMember(int id) throws SQLException;

  ItemDTO getOneItem(int id) throws SQLException;

  int addItem(ItemDTO itemDTO) throws SQLException;

  ItemDTO cancelItem(int id) throws SQLException;

  ItemDTO modifyItem(ItemDTO itemDTO) throws SQLException;
}
