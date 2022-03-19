package be.vinci.pae.biz.interest.interfaces;

public interface InterestUCC {

  /**
   * Mark the interest in an offer.
   *
   * @param id_member   the id of the member
   * @param id_offer    the id of the offer
   * @param call_wanted true if member want to be call false if not
   * @return 1 if interest good added -1 if not
   */
  int markInterest(int id_member, int id_offer, boolean call_wanted);

  /**
   * Verify if the offer exist in the DB.
   *
   * @param id_offer the if of the offer
   * @return true if exist in the DB false if not
   */
  boolean offerExist(int id_offer);

  /**
   * Verify if the member exist in the DB.
   *
   * @param id_member the if od the member
   * @return true if exist in the DB false if not
   */
  boolean memberExist(int id_member);

  /**
   * Verify if the interest exist in the DB.
   *
   * @param id_offer  the id of the offer
   * @param id_member the id of the member
   * @return true if exist in the DB false if not
   */
  boolean interestExist(int id_offer, int id_member);
}
