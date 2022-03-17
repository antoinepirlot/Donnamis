package be.vinci.pae.dal.interfaces.offer;

import be.vinci.pae.biz.interfaces.offer.OfferDTO;
import java.util.List;

public interface OfferDAO {

  /**
   * Add a new offer to the db.
   *
   * @param offerDTO the offer to add in the db
   * @return true if the offer has been created
   */
  boolean createOffer(OfferDTO offerDTO);

  List<OfferDTO> getLatestOffers();

  List<OfferDTO> getAllOffers();
}
