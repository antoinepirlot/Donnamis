package be.vinci.pae.biz.item.objects;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO itemDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public List<ItemDTO> getLatestItems() {
    dalServices.start();
    List<ItemDTO> listItemDTO = itemDAO.getDonatedItems();
    if (listItemDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return listItemDTO;
  }

  @Override
  public List<ItemDTO> getAllItems() {
    dalServices.start();
    List<ItemDTO> listItemDTO = itemDAO.getAllItems();
    if (listItemDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return listItemDTO;
  }

  @Override
  public List<ItemDTO> getAllOfferedItems() {
    dalServices.start();
    List<ItemDTO> listItemDTO = itemDAO.getAllOfferedItems();
    System.out.println(listItemDTO);
    if (listItemDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return listItemDTO;
  }

  @Override
  public ItemDTO getOneItem(int id) {
    dalServices.start();
    ItemDTO itemDTO = itemDAO.getOneItem(id);
    if (itemDTO == null) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return itemDTO;
  }

  @Override
  public boolean addItem(ItemDTO itemDTO) {
    dalServices.start();
    if (!itemDAO.addItem(itemDTO)) {
      dalServices.rollback();
    }
    return dalServices.commit();
  }

  @Override
  public ItemDTO cancelOffer(int id) {
    dalServices.start();
    ItemDTO itemDTO = itemDAO.cancelOffer(id);
    if (itemDTO == null) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return itemDTO;
  }

  @Override
  public List<ItemDTO> getAllItemsByMemberId(int id) {
    dalServices.start();
    List<ItemDTO> listItemDTO = itemDAO.getAllItemsByMemberId(id);
    if (listItemDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return listItemDTO;
  }
}
