package be.vinci.pae.biz.item.objects;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO itemDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public List<ItemDTO> getAllItems(String offerStatus) {
    try {
      dalServices.start();
      List<ItemDTO> listItemDTO = itemDAO.getAllItems(offerStatus);
      dalServices.commit();
      return listItemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public ItemDTO getOneItem(int id) {
    try {
      dalServices.start();
      ItemDTO itemDTO = itemDAO.getOneItem(id);
      dalServices.commit();
      return itemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public int addItem(ItemDTO itemDTO) {
    try {
      dalServices.start();
      int idItem = itemDAO.addItem(itemDTO);
      dalServices.commit();
      return idItem;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public ItemDTO cancelItem(int id) {
    try {
      dalServices.start();
      ItemDTO itemDTO = itemDAO.cancelItem(id);
      dalServices.commit();
      return itemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public ItemDTO modifyItem(ItemDTO itemDTO) {
    try {
      dalServices.start();
      ItemDTO modifyItem = itemDAO.modifyItem(itemDTO);
      dalServices.commit();
      return modifyItem;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getAllItemsOfAMember(int idMember) {
    try {
      dalServices.start();
      List<ItemDTO> listItemDTO = itemDAO.getAllItemsOfAMember(idMember);
      dalServices.commit();
      return listItemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getAssignedItems(int idMember) {
    try {
      this.dalServices.start();
      List<ItemDTO> listItemDTO = this.itemDAO.getAssignedItems(idMember);
      this.dalServices.commit();
      return listItemDTO;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean markItemAsGiven(ItemDTO itemDTO) {
    return this.markItemAs(true, itemDTO);
  }

  @Override
  public boolean markItemAsNotGiven(ItemDTO itemDTO) {
    return this.markItemAs(false, itemDTO);
  }

  @Override
  public int countNumberOfItemsByOfferStatus(int idMember, String offerStatus) {
    try {
      this.dalServices.start();
      int itemsNumber = this.itemDAO.countNumberOfItemsByOfferStatus(idMember, offerStatus);
      this.dalServices.commit();
      return itemsNumber;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public int countNumberOfReceivedOrNotReceivedItems(int idMember, boolean received) {
    try {
      this.dalServices.start();
      int numberOfItems = this.itemDAO.countNumberOfReceivedOrNotReceivedItems(idMember, received);
      this.dalServices.commit();
      return numberOfItems;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getMemberItemsByOfferStatus(int idMember, String offerStatus) {
    try {
      this.dalServices.start();
      List<ItemDTO> itemDTOList = this.itemDAO.getMemberItemsByOfferStatus(idMember, offerStatus);
      this.dalServices.commit();
      return itemDTOList;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getMemberReceivedItems(int idMember) {
    try {
      this.dalServices.start();
      List<ItemDTO> itemDTOList = this.itemDAO.getMemberReceivedItems(idMember);
      this.dalServices.commit();
      return itemDTOList;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  /////////////////////////////////////////////////////////
  ///////////////////////UTILS/////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * mark item as given or not given and update the recipient.
   *
   * @param given   true if it marks item as given or false to mark item as not given
   * @param itemDTO the item to update
   * @return true if the operation worked as expected otherwise false
   */
  private boolean markItemAs(boolean given, ItemDTO itemDTO) {
    try {
      boolean done;
      this.dalServices.start();
      if (given) {
        done = this.itemDAO.markItemAsGiven(itemDTO);
      } else {
        done = this.itemDAO.markItemAsNotGiven(itemDTO);
      }
      this.dalServices.commit();
      return done;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean addPhoto(int idItem, String photoName) {
    try {
      this.dalServices.start();
      boolean added = this.itemDAO.addPhoto(idItem, photoName);
      this.dalServices.commit();
      return added;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }
}
