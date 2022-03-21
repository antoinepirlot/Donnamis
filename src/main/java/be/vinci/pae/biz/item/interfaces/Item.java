package be.vinci.pae.biz.item.interfaces;

import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;

public interface Item extends ItemDTO {

  void addOffer(OfferDTO offer);
}
