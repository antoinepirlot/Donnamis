package be.vinci.pae.biz.recipient.interfaces;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.recipient.objects.RecipientImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = RecipientImpl.class)
public interface RecipientDTO {

  int getId();

  void setId(int id);

  Item getItem();

  void setItem(Item item);

  Member getMember();

  void setMember(Member member);

  String getReceived();

  void setReceived(String received);
}
