package be.vinci.pae.biz.item.interfaces;

import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import java.util.List;

public interface Item extends ItemDTO {

  List<OfferDTO> getOfferList();

  void setOfferList(List<OfferDTO> offerList);

  void addOffer(OfferDTO offer);
}
