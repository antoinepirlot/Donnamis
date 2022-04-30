package be.vinci.pae.biz.item.interfaces;

import be.vinci.pae.biz.offer.interfaces.Offer;

public interface Item extends ItemDTO {

  void addOffer(Offer offer);

}
