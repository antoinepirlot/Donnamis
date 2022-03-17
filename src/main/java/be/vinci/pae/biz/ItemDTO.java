package be.vinci.pae.biz;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ItemImpl.class)
public interface ItemDTO {

  int getId();

  void setId(int id);

  String getItem_description();

  void setItem_description(String item_description);

  int getId_item_type();

  void setId_item_type(int id_item_type);

  MemberDTO getMember();

  void setMember(MemberDTO member);

  String getPhoto();

  void setPhoto(String photo);

  String getTitle();

  void setTitle(String title);

  String getOffer_status();

  void setOffer_status(String offers_status);

}
