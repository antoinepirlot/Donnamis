package be.vinci.pae.dal.offer.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.SQLException;
import java.util.List;

public interface OfferDAO {

  /**
   * Add a new offer to the db.
   *
   * @param offerDTO the offer to add in the db
   * @return true if the offer has been created
   */
  boolean createOffer(OfferDTO offerDTO) throws SQLException;

  List<OfferDTO> getAllOffers(String offerStatus) throws SQLException;

  OfferDTO getOne(int id) throws SQLException;

  boolean offerExist(OfferDTO offerDTO) throws SQLException;

  /**
   * Get the last offer of the itemDTO.
   *
   * @param itemDTO the item that need offers to be added
   * @return the more recent offer of itemDTO
   * @throws SQLException if something is wrong in the
   */
  @JsonIgnore
  OfferDTO getLastOfferOf(ItemDTO itemDTO) throws SQLException;
}
