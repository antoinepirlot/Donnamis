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
    try {
      dalServices.start();
      List<ItemDTO> listItemDTO = itemDAO.getAllItems(offerStatus);
      dalServices.commit();
      return listItemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public ItemDTO getOneItem(int id) throws SQLException {
    try {
      dalServices.start();
      ItemDTO itemDTO = itemDAO.getOneItem(id);
      dalServices.commit();
      return itemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public int addItem(ItemDTO itemDTO) throws SQLException {
    try {
      dalServices.start();
      int id_item = itemDAO.addItem(itemDTO);
      dalServices.commit();
      return id_item;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public ItemDTO cancelItem(int id) throws SQLException {
    try {
      dalServices.start();
      ItemDTO itemDTO = itemDAO.cancelItem(id);
      dalServices.commit();
      return itemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public ItemDTO modifyItem(ItemDTO itemDTO) throws SQLException {
    try {
      dalServices.start();
      ItemDTO modifyItem = itemDAO.modifyItem(itemDTO);
      dalServices.commit();
      return modifyItem;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public List<ItemDTO> getAllItemsOfAMember(int idMember) throws SQLException {
    try {
      dalServices.start();
      List<ItemDTO> listItemDTO = itemDAO.getAllItemsOfAMember(idMember);
      dalServices.commit();
      return listItemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public List<ItemDTO> getAssignedItems(int idMember) throws SQLException {
    try {
      this.dalServices.start();
      List<ItemDTO> listItemDTO = this.itemDAO.getAssignedItems(idMember);
      this.dalServices.commit();
      return listItemDTO;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw e;
    }
  }
}
