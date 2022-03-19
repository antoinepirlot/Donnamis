package be.vinci.pae.biz.interfaces.offer;

import java.util.List;

public interface OfferUCC {

  boolean createOffer(OfferDTO offerDTO);

  List<OfferDTO> getLatestOffers();

  List<OfferDTO> getAllOffers();
}
