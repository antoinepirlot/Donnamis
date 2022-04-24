package be.vinci.pae.biz.interest.interfaces;

import java.sql.SQLException;

public interface InterestUCC {

  /**
   * Mark the interest in an offer.
   *
   * @param interestDTO the interest to add
   */
  boolean markInterest(InterestDTO interestDTO) throws SQLException;

  /**
   * Verify if the interest exist in the DB.
   *
   * @param interestDTO the interest to check
   * @return true if exist in the DB false if not
   */
  boolean interestExist(InterestDTO interestDTO) throws SQLException;
}
