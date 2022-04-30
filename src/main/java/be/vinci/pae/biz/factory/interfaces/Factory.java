package be.vinci.pae.biz.factory.interfaces;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;

public interface Factory {

  ItemDTO getItem();

  MemberDTO getMember();

  AddressDTO getAddress();

  OfferDTO getOffer();

  ItemsTypeDTO getItemType();

  RecipientDTO getRecipient();

  RefusalDTO getRefusal();

  RatingDTO getRating();
}