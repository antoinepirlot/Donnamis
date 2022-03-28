package be.vinci.pae.biz.offer.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import java.util.List;

public interface OfferUCC {

  /**
   * Asks UCC to create an offer.
   *
   * @param offerDTO the offer to create
   * @return true if the offer has been created otherwise false
   */
  boolean createOffer(OfferDTO offerDTO);

  /**
   * Asks UCC to get a list of the latest offers.
   *
   * @return the list of the latest offers
   */
  List<OfferDTO> getLatestOffers();

  /**
   * Asks UCC to get a list of all offers.
   *
   * @return the list of all offers
   */
  List<OfferDTO> getAllOffers();

  OfferDTO getLatestItemOffer(ItemDTO itemDTO);

  /**
   * Asks UCC to get one offer identified by its id.
   *
   * @param id the offer's id
   * @return the offer if it exists, otherwise null
   */
  OfferDTO getOneOffer(int id);
}
