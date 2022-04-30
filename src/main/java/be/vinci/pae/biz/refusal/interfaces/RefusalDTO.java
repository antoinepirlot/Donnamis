package be.vinci.pae.biz.refusal.interfaces;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.refusal.objects.RefusalImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = RefusalImpl.class)
public interface RefusalDTO {

  int getIdRefusal();

  void setIdRefusal(int idRefusal);

  MemberDTO getMember();

  void setMember(MemberDTO member);

  String getText();

  void setText(String text);

  int getVersion();

  void setVersion(int version);
}
