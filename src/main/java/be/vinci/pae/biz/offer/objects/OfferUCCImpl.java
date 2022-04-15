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
  public boolean createOffer(OfferDTO offerDTO) {
    //Cette ligne n'est nécessaire que si on doit faire appel à une fonction de l'interface Offer
    //Offer offer = (Offer) offerDTO;
    dalServices.start();
    if (!this.offerDAO.createOffer(offerDTO)) {
      dalServices.rollback();
    }
    return dalServices.commit();
  }

  @Override
  public List<OfferDTO> getLatestOffers() {
    dalServices.start();
    List<OfferDTO> listOfferDTO = offerDAO.getLatestOffers();
    if (listOfferDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return listOfferDTO;
  }

  @Override
  public List<OfferDTO> getAllOffers() {
    dalServices.start();
    List<OfferDTO> listOfferDTO = offerDAO.getAllOffers();
    if (listOfferDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return listOfferDTO;
  }

  @Override
  public OfferDTO getOneOffer(int id) {
    dalServices.start();
    OfferDTO offerDTO = offerDAO.getOne(id);
    if (offerDTO == null) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return offerDTO;
  }

  @Override
  public void getAllOffersOf(ItemDTO itemDTO) {
    Item item = (Item) itemDTO;
    dalServices.start();
    try {
      Offer offerToAdd = (Offer) this.offerDAO.getLastOfferOf(item);
      item.addOffer(offerToAdd);
    } catch (SQLException e) {
      dalServices.rollback();
    }
    dalServices.commit();
  }

  @Override
  public boolean offerExist(OfferDTO offerDTO) {
    dalServices.start();
    if (!this.offerDAO.offerExist(offerDTO)) {
      dalServices.rollback();
      return false;
    }
    dalServices.commit();
    return true;
  }
}
