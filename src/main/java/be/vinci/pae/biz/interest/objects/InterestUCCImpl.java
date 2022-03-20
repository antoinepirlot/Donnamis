package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import dal.interest.interfaces.InterestDAO;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;

  /**
   * Mark the interest in an offer.
   *
   * @param idMember   the id of the member
   * @param idOffer    the id of the offer
   * @param callWanted true if member want to be call false if not
   * @return 1 if interest good added -1 if not
   */
  @Override
  public int markInterest(int idMember, int idOffer, boolean callWanted) {
    LocalDate date = LocalDate.now();
    return interestDAO.markInterest(idMember, idOffer, callWanted, date);
  }

  /**
   * Verify if the offer exist in the DB.
   *
   * @param idOffer the if of the offer
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean offerExist(int idOffer) {
    return interestDAO.offerExist(idOffer);
  }

  /**
   * Verify if the member exist in the DB.
   *
   * @param idMember the if od the member
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean memberExist(int idMember) {
    return interestDAO.memberExist(idMember);
  }

  /**
   * Verify if the interest exist in the DB.
   *
   * @param idOffer  the id of the offer
   * @param idMember the id of the member
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean interestExist(int idOffer, int idMember) {
    return interestDAO.interestExist(idOffer, idMember);
  }

}
