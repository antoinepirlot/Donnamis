package be.vinci.pae.dal.offer.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public interface OfferDAO {

  /**
   * Add a new offer to the db.
   *
   * @param offerDTO the offer to add in the db
   * @return true if the offer has been created
   */
  boolean createOffer(OfferDTO offerDTO);

  List<OfferDTO> getAllOffers(String offerStatus);

  OfferDTO getOne(int id);

  boolean offerExist(OfferDTO offerDTO);

  /**
   * Get the 2 last offer of the itemDTO.
   *
   * @param itemDTO the item that need offers to be added
   * @return the more recent offer of itemDTO
   */
  @JsonIgnore
  List<OfferDTO> getLastTwoOffersOf(ItemDTO itemDTO);
}
