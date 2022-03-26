package be.vinci.pae.biz.item.objects;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import jakarta.inject.Inject;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO itemDAO;

  @Override
  public List<ItemDTO> getLatestItems() {
    return itemDAO.getLatestItems();
  }

  @Override
  public List<ItemDTO> getAllItems() {
    return itemDAO.getAllItems();
  }

  @Override
  public List<ItemDTO> getAllOfferedItems() {
    return this.itemDAO.getAllOfferedItems();
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
