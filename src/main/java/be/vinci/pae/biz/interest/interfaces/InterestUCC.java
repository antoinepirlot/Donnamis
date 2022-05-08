package be.vinci.pae.biz.interest.interfaces;

public interface InterestUCC {

  /**
   * Mark the interest in an offer.
   */
  void markInterest(InterestDTO interestDTO);

  /**
   * Verify if the interest exist in the DB.
   *
   * @param interestDTO the interest to check
   * @return true if exist in the DB false if not
   */
  boolean interestExist(InterestDTO interestDTO);
}
