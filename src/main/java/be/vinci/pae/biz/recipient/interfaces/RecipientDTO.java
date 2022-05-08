package be.vinci.pae.biz.recipient.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.recipient.objects.RecipientImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = RecipientImpl.class)
public interface RecipientDTO {

  int getId();

  void setId(int id);

  ItemDTO getItem();

  void setItem(ItemDTO itemDTO);

  MemberDTO getMember();

  void setMember(MemberDTO memberDTO);

  String getReceived();

  void setReceived(String received);

  int getVersion();

  void setVersion(int version);
}
