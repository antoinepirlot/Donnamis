package be.vinci.pae.dal.offer.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import java.sql.SQLException;
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

  /**
   * Add offers to itemDTO offer list. (no insert into the db)
   * @param itemDTO the item that need offers to be added
   * @throws SQLException if something is wrong in the
   */
  void addOffersTo(ItemDTO itemDTO) throws SQLException;
}
