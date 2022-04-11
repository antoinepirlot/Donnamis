package be.vinci.pae.biz.interest.interfaces;

import be.vinci.pae.biz.interest.objects.InterestImpl;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;

@JsonDeserialize(as = InterestImpl.class)
public interface InterestDTO {

  int getId();

  void setId(int id);

  boolean isCallWanted();

  void setCallWanted(boolean callWanted);

  int getIdItem();

  void setIdItem(int idItem);

  MemberDTO getMember();

  void setMember(MemberDTO idMember);

  Date getDate();

  void setDate(Date date);
}
