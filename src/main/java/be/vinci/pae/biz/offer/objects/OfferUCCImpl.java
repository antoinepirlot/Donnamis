package be.vinci.pae.biz.offer.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class OfferUCCImpl implements OfferUCC {

  @Inject
  private OfferDAO offerDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public boolean createOffer(OfferDTO offerDTO) {
    try {
      dalServices.start();
      boolean isCreated = this.offerDAO.createOffer(offerDTO);
      dalServices.commit();
      if (!isCreated) {
        String message = "Error while creating new offer.";
        throw new FatalException(message);
      }
      return true;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<OfferDTO> getAllOffers(String offerStatus) {
    try {
      dalServices.start();
      List<OfferDTO> listOfferDTO = offerDAO.getAllOffers(offerStatus);
      dalServices.commit();
      if (listOfferDTO == null) {
        throw new ObjectNotFoundException("No offers into the database.");
      }
      return listOfferDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public OfferDTO getOneOffer(int id) {
    try {
      dalServices.start();
      OfferDTO offerDTO = offerDAO.getOne(id);
      dalServices.commit();
      if (offerDTO == null) {
        throw new ObjectNotFoundException("No offers into the database.");
      }
      return offerDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public void getLastTwoOffersOf(ItemDTO itemDTO) {
    Item item = (Item) itemDTO;
    try {
      dalServices.start();
      List<OfferDTO> offerToAdd = this.offerDAO.getLastTwoOffersOf(item);
      dalServices.commit();
      item.setOfferList(offerToAdd);
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean offerExist(OfferDTO offerDTO) {
    try {
      dalServices.start();
      boolean doesExist = this.offerDAO.offerExist(offerDTO);
      dalServices.commit();
      return doesExist;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public int getNumberOfInterestedMemberOf(int idItem) {
    try {
      dalServices.start();
      int count = this.offerDAO.getNumberOfInterestedMemberOf(idItem);
      dalServices.commit();
      return count;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }
}
