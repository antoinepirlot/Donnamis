package be.vinci.pae.biz.offer.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class OfferUCCImpl implements OfferUCC {

  @Inject
  private OfferDAO offerDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public boolean createOffer(OfferDTO offerDTO) throws SQLException {
    try {
      dalServices.start();
      boolean isCreated = this.offerDAO.createOffer(offerDTO);
      dalServices.commit();
      return isCreated;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public List<OfferDTO> getAllOffers(String offerStatus) throws SQLException {
    try {
      dalServices.start();
      List<OfferDTO> listOfferDTO = offerDAO.getAllOffers(offerStatus);
      dalServices.commit();
      return listOfferDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public OfferDTO getOneOffer(int id) throws SQLException {
    try {
      dalServices.start();
      OfferDTO offerDTO = offerDAO.getOne(id);
      dalServices.commit();
      return offerDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public void getAllOffersOf(ItemDTO itemDTO) throws SQLException {
    Item item = (Item) itemDTO;
    try {
      dalServices.start();
      Offer offerToAdd = (Offer) this.offerDAO.getLastOfferOf(item);
      dalServices.commit();
      item.addOffer(offerToAdd);
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public boolean offerExist(OfferDTO offerDTO) throws SQLException {
    try {
      dalServices.start();
      boolean doesExist = this.offerDAO.offerExist(offerDTO);
      dalServices.commit();
      return doesExist;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }
}