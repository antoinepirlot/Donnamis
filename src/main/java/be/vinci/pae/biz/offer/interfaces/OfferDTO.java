package be.vinci.pae.biz.offer.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.objects.OfferImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Date;

@JsonDeserialize(as = OfferImpl.class)
public interface OfferDTO {

  int getIdOffer();

  void setIdOffer(int idOffer);

  Date getDate();

  void setDate(Date date);

  String getTime_slot();

  void setTime_slot(String time_slot);

  ItemDTO getItem();

  void setItem(ItemDTO itemDTO);
}
