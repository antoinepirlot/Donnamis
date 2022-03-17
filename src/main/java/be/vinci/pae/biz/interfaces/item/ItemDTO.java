package be.vinci.pae.biz.interfaces.item;

import be.vinci.pae.biz.objects.item.ItemImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ItemImpl.class)
public interface ItemDTO {

  int getId();

  void setId(int id);

  String getItemDescription();

  void setItemDescription(String itemDescription);

  ItemTypeDTO getItemType();

  void setItemType(ItemTypeDTO itemType);

  MemberDTO getMember();

  void setMember(MemberDTO member);

  String getPhoto();

  void setPhoto(String photo);

  String getTitle();

  void setTitle(String title);

  String getOfferStatus();

  void setOfferStatus(String offers_status);

}
