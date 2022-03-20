package be.vinci.pae.biz.interest.interfaces;

import java.util.Date;

public interface InterestDTO {

  int getIdInterest();

  void setId(int id);

  boolean isCallWanted();

  void setCallWanted(boolean callWanted);

  int getIdOffer();

  void setIdOffer(int idOffer);

  int getIdMember();

  void setIdMember(int idMember);

  Date getDate();

  void setDate(Date date);
}
