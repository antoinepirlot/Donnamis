package be.vinci.pae.biz.objects.offer;

import be.vinci.pae.biz.interfaces.offer.OfferDTO;
import be.vinci.pae.biz.interfaces.offer.OfferUCC;
import be.vinci.pae.dal.interfaces.offer.OfferDAO;
import jakarta.inject.Inject;
import java.util.List;

public class OfferUCCImpl implements OfferUCC {

  @Inject
  private OfferDAO offerDAO;

  @Override
  public boolean createOffer(OfferDTO offerDTO) {
    //Cette ligne n'est nécessaire que si on doit faire appel à une fonction de l'interface Offer
    //Offer offer = (Offer) offerDTO;
    return this.offerDAO.createOffer(offerDTO);
  }

  @Override
  public List<OfferDTO> getLatestOffers() {
    return offerDAO.getLatestOffers();
  }

  @Override
  public List<OfferDTO> getAllOffers() {
    return offerDAO.getAllOffers();
  }
}
