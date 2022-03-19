package be.vinci.pae.biz.factory.interfaces;

import be.vinci.pae.biz.items_type.interfaces.ItemTypeDTO;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.address.interfaces.AddressDTO;

public interface Factory {

  ItemDTO getItem();

  MemberDTO getMember();

  AddressDTO getAddress();

  OfferDTO getOffer();

  ItemTypeDTO getItemType();

}