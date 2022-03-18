package be.vinci.pae.biz;

import java.util.List;

public interface OfferUCC {

  boolean createOffer(OfferDTO offerDTO);

  List<OfferDTO> getLatestOffers();

  List<OfferDTO> getAllOffers();

  OfferDTO getOneOffer(int id);
}
