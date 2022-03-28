package be.vinci.pae.dal.offer.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
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

  OfferDTO getOne(int id);

  OfferDTO getLatestItemOffer(ItemDTO itemDTO);
}
