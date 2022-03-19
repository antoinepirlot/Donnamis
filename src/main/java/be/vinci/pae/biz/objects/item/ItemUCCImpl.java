package be.vinci.pae.biz.objects.item;

import be.vinci.pae.biz.interfaces.item.ItemDTO;
import be.vinci.pae.biz.interfaces.item.ItemUCC;
import be.vinci.pae.dal.interfaces.item.ItemDAO;
import jakarta.inject.Inject;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO itemDAO;

  /**
   * Get the latest items from the database.
   *
   * @return List of the latest items offered
   */
  @Override
  public List<ItemDTO> getLatestItems() {
    return itemDAO.getLatestItems();
  }

  /**
   * Get the latest items from the database.
   *
   * @return List of all items offered
   */
  @Override
  public List<ItemDTO> getAllItems() {
    return itemDAO.getAllItems();
  }

  @Override
  public ItemDTO getOneItem(int id) {
    return itemDAO.getOneItem(id);
  }

  @Override
  public boolean addItem(ItemDTO itemDTO) {
    return this.itemDAO.addItem(itemDTO);
  }

  @Override
  public ItemDTO cancelOffer(int id) {
    return itemDAO.cancelOffer(id);
  }
}
