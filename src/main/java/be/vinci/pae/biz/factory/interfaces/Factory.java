package be.vinci.pae.biz.factory.interfaces;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;

public interface Factory {

  ItemDTO getItem();

  MemberDTO getMember();

  AddressDTO getAddress();

  OfferDTO getOffer();

  ItemsTypeDTO getItemType();

}