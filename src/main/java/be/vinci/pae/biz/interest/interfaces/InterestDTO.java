package be.vinci.pae.biz.interest.interfaces;

import be.vinci.pae.biz.interest.objects.InterestImpl;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.sql.Timestamp;

@JsonDeserialize(as = InterestImpl.class)
public interface InterestDTO {

  int getId();

  void setId(int id);

  boolean isCallWanted();

  void setCallWanted(boolean callWanted);

  OfferDTO getOffer();

  void setOffer(OfferDTO offer);

  MemberDTO getMember();

  void setMember(MemberDTO idMember);

  Timestamp getDate();

  void setDate(Timestamp date);

  int getVersion();

  void setVersion(int version);
}
