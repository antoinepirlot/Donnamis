package be.vinci.pae.biz;

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

  @Override
  public ItemTypeDTO getItemType() {
    return new ItemTypeImpl();
  }
}
