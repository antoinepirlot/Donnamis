package be.vinci.pae.biz;

public class FactoryImpl implements Factory {

  @Override
  public MemberDTO getMember() {
    return new MemberImpl();
  }

  @Override
  public OfferDTO getOffer() {
    return null;
  }

  @Override
  public Item getItem() {
    return new ItemImpl();
  }
}
