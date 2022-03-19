package be.vinci.pae.biz.factory;

import be.vinci.pae.biz.interfaces.item.Item;
import be.vinci.pae.biz.objects.item.ItemImpl;
import be.vinci.pae.biz.interfaces.offer.OfferDTO;
import be.vinci.pae.biz.interfaces.member.MemberDTO;
import be.vinci.pae.biz.objects.member.MemberImpl;
import be.vinci.pae.biz.interfaces.member.address.AddressDTO;
import be.vinci.pae.biz.objects.member.address.AddressImpl;
import be.vinci.pae.biz.objects.offer.OfferImpl;

public class FactoryImpl implements Factory {

  @Override
  public MemberDTO getMember() {
    return new MemberImpl();
  }

  @Override
  public OfferDTO getOffer() {
    return new OfferImpl();
  }

  @Override
  public Item getItem() {
    return new ItemImpl();
  }

  @Override
  public AddressDTO getAddress() {
    return new AddressImpl();
  }
}
