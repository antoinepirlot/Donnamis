package be.vinci.pae.biz.offer.interfaces;

import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.objects.OfferImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Timestamp;

@JsonDeserialize(as = OfferImpl.class)
public interface OfferDTO {

  int getId();

  void setId(int id);

  Timestamp getDate();

  void setDate(Timestamp date);

  String getTimeSlot();

  void setTimeSlot(String timeSlot);

  int getIdItem();

  void setIdItem(int idItem);

  Member getMember();

  void setMember(MemberDTO memberDTO);

  int getNumberOfInterests();

  void setNumberOfInterests(int numberOfInterests);

  int getVersion();

  void setVersion(int version);

  String toString();
}
