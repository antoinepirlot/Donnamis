package be.vinci.pae.biz.item.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class ItemUCCImpl implements ItemUCC {

  @Inject
  private ItemDAO itemDAO;
  @Inject
  private OfferUCC offerUCC;
  @Inject
  private MemberUCC memberUCC;
  @Inject
  private DALServices dalServices;

  @Override
  public List<ItemDTO> getAllItems(String offerStatus) {
    try {
      dalServices.start();
      List<ItemDTO> listItemDTO = itemDAO.getAllItems(offerStatus);
      dalServices.commit();
      if (listItemDTO == null) {
        throw new ObjectNotFoundException(
            "There's no items matching with offer status: " + offerStatus + "."
        );
      }
      for (ItemDTO itemDTO : listItemDTO) {
        this.offerUCC.getLastTwoOffersOf(itemDTO);
      }
      return listItemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public ItemDTO getOneItem(ItemDTO itemDTO, int idItem) {
    if (itemDTO != null) {
      idItem = itemDTO.getId();
    }
    if (!this.itemExists(idItem)) {
      throw new ObjectNotFoundException("The item " + idItem + " doesn't exist.");
    }
    try {
      dalServices.start();
      ItemDTO dbItemDTO = itemDAO.getOneItem(idItem);
      dalServices.commit();
      if (dbItemDTO == null) {
        throw new ObjectNotFoundException("No item matching id: " + idItem);
      }
      return dbItemDTO;
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
      if (idItem == -1) {
        String message = "The items can't be added to the db due to a unexpected error";
        throw new FatalException(message);
      }
      Offer offerDTO = (Offer) itemDTO.getOfferList().get(0);
      offerDTO.setIdItem(idItem);
      if (!this.offerUCC.createOffer(offerDTO)) {
        String message = "The offer can't be added to the db due to a unexpected error";
        throw new FatalException(message);
      }
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
  public void modifyItem(ItemDTO itemDTO) {
    this.checkVersion(itemDTO);
    try {
      dalServices.start();
      boolean modified = itemDAO.modifyItem(itemDTO);
      dalServices.commit();
      if (!modified) {
        throw new FatalException("Item " + itemDTO.getId() + "not modified");
      }
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getAllItemsOfAMember(int idMember) {
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("This member doesn't exists.");
    }
    try {
      dalServices.start();
      List<ItemDTO> listItemDTO = itemDAO.getAllItemsOfAMember(idMember);
      dalServices.commit();
      if (listItemDTO == null) {
        throw new ObjectNotFoundException(
            "There's no items matching with member's id: " + idMember + "."
        );
      }
      for (ItemDTO itemDTO : listItemDTO) {
        this.offerUCC.getLastTwoOffersOf(itemDTO);
      }
      return listItemDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getAssignedItems(int idMember) {
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("No member matching with id " + idMember);
    }
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
  public List<ItemDTO> getGivenItems(int idMember) {
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("No member matching with id " + idMember);
    }
    try {
      this.dalServices.start();
      List<ItemDTO> listItemDTO = this.itemDAO.getGivenItems(idMember);
      this.dalServices.commit();
      return listItemDTO;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public void markItemAsGiven(ItemDTO itemDTO) {
    this.markItemAs(true, itemDTO);
  }

  @Override
  public void markItemAsNotGiven(ItemDTO itemDTO) {
    this.markItemAs(false, itemDTO);
  }

  @Override
  public int countNumberOfItemsByOfferStatus(int idMember, String offerStatus) {
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("The member " + idMember + " doesn't exist.");
    }
    try {
      this.dalServices.start();
      int count = this.itemDAO.countNumberOfItemsByOfferStatus(idMember, offerStatus);
      this.dalServices.commit();
      if (count == -1) {
        throw new FatalException("count number of items returned -1.");
      }
      return count;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public int countNumberOfReceivedOrNotReceivedItems(int idMember, boolean received) {
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("The member " + idMember + " doesn't exist.");
    }
    try {
      this.dalServices.start();
      int count = this.itemDAO.countNumberOfReceivedOrNotReceivedItems(idMember, received);
      this.dalServices.commit();
      if (count == -1) {
        throw new FatalException("Count number returned -1");
      }
      return count;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getMemberItemsByOfferStatus(int idMember, String offerStatus) {
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("No member matching the id: " + idMember);
    }
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
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("No member matching the id: " + idMember);
    }
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

  @Override
  public void addPhoto(int idItem, String photoName) {
    if (!this.itemExists(idItem)) {
      throw new ObjectNotFoundException("No item matching the id: " + idItem);
    }
    try {
      this.dalServices.start();
      boolean added = this.itemDAO.addPhoto(idItem, photoName);
      this.dalServices.commit();
      if (!added) {
        throw new FatalException("The image hasn't been added into the database");
      }
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean itemExists(int idItem) {
    try {
      this.dalServices.start();
      boolean exists = this.itemDAO.itemExists(idItem);
      this.dalServices.commit();
      return exists;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<ItemDTO> getAllPublicItems() {
    try {
      this.dalServices.start();
      List<ItemDTO> itemDTOList = this.itemDAO.getAllPublicItems();
      this.dalServices.commit();
      return itemDTOList;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  /**
   * Check the version of item.
   *
   * @param itemDTO the item to check
   */
  private void checkVersion(ItemDTO itemDTO) {
    try {
      this.dalServices.start();
      Item dbItem = (Item) this.getOneItem(itemDTO, -1);
      if (itemDTO.getVersion() != dbItem.getVersion()) {
        throw new FatalException("Wrong version");
      }
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
   */
  private void markItemAs(boolean given, ItemDTO itemDTO) {
    if (!this.itemExists(itemDTO.getId())) {
      throw new ObjectNotFoundException("No item matching the id: " + itemDTO.getId());
    }
    this.checkVersion(itemDTO);
    try {
      boolean done;
      this.dalServices.start();
      if (given) {
        done = this.itemDAO.markItemAsGiven(itemDTO);
      } else {
        done = this.itemDAO.markItemAsNotGiven(itemDTO);
      }
      this.dalServices.commit();
      if (!done) {
        if (given) {
          throw new FatalException("marking item " + itemDTO.getId() + " as given failed.");
        } else {
          throw new FatalException("marking item " + itemDTO.getId() + " as not given failed.");
        }
      }
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }
}
