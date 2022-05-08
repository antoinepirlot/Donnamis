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
   * Asks UCC to get a list of all offers.
   *
   * @return the list of all offers
   */
  List<OfferDTO> getAllOffers(String offerStatus);

  /**
   * Asks UCC to get one offer identified by its id.
   *
   * @param id the offer's id
   * @return the offer if it exists, otherwise null
   */
  OfferDTO getOneOffer(int id);

  void getLastTwoOffersOf(ItemDTO itemDTO);

  /**
   * Verify if the offer exist in the DB.
   *
   * @param offerDTO the offer to check
   * @return true if exist in the DB false if not
   */
  boolean offerExist(OfferDTO offerDTO);

  /**
   * Count the number of interested member for the last offer of the item identified by its id.
   *
   * @param idItem the item's id
   * @return the number of interested member of the item's last offer
   */
  int getNumberOfInterestedMemberOf(int idItem);
}
