package be.vinci.pae.biz.interest;

public interface InterestUCC {

  int markInterest(int id_member, int id_offer, boolean call_wanted);

  boolean offerExist(int id_offer);

  boolean memberExist(int id_member);
}
