package be.vinci.pae.biz.factory.objects;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.address.objects.AddressImpl;
import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.objects.ItemImpl;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.itemstype.objects.ItemsTypeImpl;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.objects.OfferImpl;
import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.rating.objects.RatingImpl;
import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.recipient.objects.RecipientImpl;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.biz.refusal.objects.RefusalImpl;

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
  public ItemDTO getItem() {
    return new ItemImpl();
  }

  @Override
  public AddressDTO getAddress() {
    return new AddressImpl();
  }

  @Override
  public ItemsTypeDTO getItemType() {
    return new ItemsTypeImpl();
  }

  @Override
  public RecipientDTO getRecipient() {
    return new RecipientImpl();
  }

  @Override
  public RefusalDTO getRefusal() {
    return new RefusalImpl();
  }

  @Override
  public RatingDTO getRating() {
    return new RatingImpl();
  }
}

