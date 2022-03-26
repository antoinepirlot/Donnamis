package be.vinci.pae.biz.interest.interfaces;

import be.vinci.pae.biz.member.interfaces.MemberDTO;

public interface InterestUCC {

  /**
   * Mark the interest in an offer.
   *
   * @param memberDTO   the member
   * @param idOffer    the id of the offer
   * @param callWanted true if member want to be call false if not
   * @return 1 if interest good added -1 if not
   */
  int markInterest(MemberDTO memberDTO, int idOffer, boolean callWanted);

  /**
   * Verify if the offer exist in the DB.
   *
   * @param idOffer the if of the offer
   * @return true if exist in the DB false if not
   */
  boolean offerExist(int idOffer);

  /**
   * Verify if the member exist in the DB.
   *
   * @param memberDTO the if od the member
   * @return true if exist in the DB false if not
   */
  boolean memberExist(MemberDTO memberDTO);

  /**
   * Verify if the interest exist in the DB.
   *
   * @param idOffer  the id of the offer
   * @param memberDTO the member
   * @return true if exist in the DB false if not
   */
  boolean interestExist(int idOffer, MemberDTO memberDTO);
}
