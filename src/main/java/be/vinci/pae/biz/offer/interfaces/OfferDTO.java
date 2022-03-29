package be.vinci.pae.biz.offer.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.objects.OfferImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Date;

@JsonDeserialize(as = OfferImpl.class)
public interface OfferDTO {

  int getId();

  void setId(int id);

  Date getDate();

  void setDate(Date date);

  String getTimeSlot();

  void setTimeSlot(String timeSlot);

  int getIdItem();

  void setIdItem(int idItem);

  Member getMember();

  void setMember(MemberDTO memberDTO);

  String toString();
}
