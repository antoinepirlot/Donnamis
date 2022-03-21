package be.vinci.pae.biz.interest.interfaces;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import java.util.Date;

public interface InterestDTO {

  int getId();

  void setId(int id);

  boolean isCallWanted();

  void setCallWanted(boolean callWanted);

  OfferDTO getOffer();

  void setOffer(OfferDTO offer);

  MemberDTO getMember();

  void setMember(MemberDTO idMember);

  Date getDate();

  void setDate(Date date);
}
