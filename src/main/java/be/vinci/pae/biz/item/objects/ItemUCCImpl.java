package be.vinci.pae.biz.item.objects;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO itemDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public List<ItemDTO> getAllItems(String offerStatus) throws SQLException {
    dalServices.start();
    List<ItemDTO> listItemDTO = itemDAO.getAllItems(offerStatus);
    if (listItemDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return listItemDTO;
  }

  @Override
  public ItemDTO getOneItem(int id) throws SQLException {
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
  public int addItem(ItemDTO itemDTO) throws SQLException {
    dalServices.start();
    int id_item = itemDAO.addItem(itemDTO);
    if (id_item == -1) {
      dalServices.rollback();
    }
    dalServices.commit();
    return id_item;
  }

  @Override
  public ItemDTO cancelOffer(int id) throws SQLException {
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
  public List<ItemDTO> getAllItemsOfAMember(int idMember) throws SQLException {
    dalServices.start();
    List<ItemDTO> listItemDTO = itemDAO.getAllItemsOfAMember(idMember);
    if (listItemDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return listItemDTO;
  }
}
