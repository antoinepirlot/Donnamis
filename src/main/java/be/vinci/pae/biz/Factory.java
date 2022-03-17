package be.vinci.pae.biz;

public interface Factory {

  ItemDTO getItem();

  MemberDTO getMember();

  AddressDTO getAddress();

  OfferDTO getOffer();

  ItemTypeDTO getItemType();

}
