package be.vinci.pae.biz.interfaces.offer;

import be.vinci.pae.biz.interfaces.item.ItemDTO;
import be.vinci.pae.biz.objects.offer.OfferImpl;
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
