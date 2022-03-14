package be.vinci.pae.dal;

import be.vinci.pae.biz.OfferDTO;

public interface OfferDAO {

  boolean createOffer(OfferDTO offerDTO);
}
