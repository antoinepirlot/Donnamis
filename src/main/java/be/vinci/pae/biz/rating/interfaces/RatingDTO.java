package be.vinci.pae.biz.rating.interfaces;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.rating.objects.RatingImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = RatingImpl.class)
public interface RatingDTO {

  int getId();

  void setId(int id);

  ItemDTO getItem();

  void setItem(ItemDTO itemDTO);

  int getRating();

  void setRating(int rating);

  String getText();

  void setText(String text);

  MemberDTO getMember();

  void setMember(MemberDTO memberDTO);

  int getVersion();

  void setVersion(int version);
}
