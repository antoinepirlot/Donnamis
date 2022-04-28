package be.vinci.pae.biz.item.interfaces;

import be.vinci.pae.biz.item.objects.ItemImpl;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonDeserialize(as = ItemImpl.class)
public interface ItemDTO {

  int getId();

  void setId(int id);

  String getItemDescription();

  void setItemDescription(String itemDescription);

  ItemsTypeDTO getItemType();

  void setItemType(ItemsTypeDTO itemType);

  MemberDTO getMember();

  void setMember(MemberDTO member);

  String getPhoto();

  void setPhoto(String photo);

  String getTitle();

  void setTitle(String title);

  String getOfferStatus();

  void setOfferStatus(String offersStatus);

  List<OfferDTO> getOfferList();

  void setOfferList(List<OfferDTO> offerList);

  OfferDTO getLastOffer();

  void setLastOffer(OfferDTO lastOffer);

  int getVersion();

  void setVersion(int version);
}
