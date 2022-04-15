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

  ItemDTO getOneItem(int id);

  int addItem(ItemDTO itemDTO);

  ItemDTO cancelOffer(int id);

  List<ItemDTO> getAllItemsOfAMember(int id);

  /**
   * Add the latest offer date into the database.
   *
   * @param itemDTO the item the latest offer date is added
   * @return true if this insert was done, otherwise false
   */
  boolean updateLatestOfferDate(ItemDTO itemDTO);
}
