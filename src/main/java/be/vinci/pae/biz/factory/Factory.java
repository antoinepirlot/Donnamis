package be.vinci.pae.biz.factory;

import be.vinci.pae.biz.interfaces.item.items_type.ItemTypeDTO;
import be.vinci.pae.biz.interfaces.item.ItemDTO;
import be.vinci.pae.biz.interfaces.offer.OfferDTO;
import be.vinci.pae.biz.interfaces.member.MemberDTO;
import be.vinci.pae.biz.interfaces.member.address.AddressDTO;

public interface Factory {

  ItemDTO getItem();

  MemberDTO getMember();

  AddressDTO getAddress();

  OfferDTO getOffer();

  ItemTypeDTO getItemType();

}