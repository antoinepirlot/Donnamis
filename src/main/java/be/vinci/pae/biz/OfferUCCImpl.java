package be.vinci.pae.biz;

import be.vinci.pae.dal.OfferDAO;
import jakarta.inject.Inject;

public class OfferUCCImpl implements OfferUCC {

  @Inject
  private OfferDAO offerDAO;

  @Override
  public boolean createOffer(OfferDTO offerDTO) {
    Offer offer = (Offer) offerDTO;
    return this.offerDAO.createOffer(offerDTO);
  }
}
