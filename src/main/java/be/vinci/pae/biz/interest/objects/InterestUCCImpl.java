package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;

  /**
   * Mark the interest in an offer.
   *
   * @param id_member   the id of the member
   * @param id_offer    the id of the offer
   * @param call_wanted true if member want to be call false if not
   * @return 1 if interest good added -1 if not
   */
  @Override
  public int markInterest(int id_member, int id_offer, boolean call_wanted) {
    LocalDate date = LocalDate.now();
    return interestDAO.markInterest(id_member, id_offer, call_wanted, date);
  }

  /**
   * Verify if the offer exist in the DB.
   *
   * @param id_offer the if of the offer
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean offerExist(int id_offer) {
    return interestDAO.offerExist(id_offer);
  }

  /**
   * Verify if the member exist in the DB.
   *
   * @param id_member the if od the member
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean memberExist(int id_member) {
    return interestDAO.memberExist(id_member);
  }

  /**
   * Verify if the interest exist in the DB.
   *
   * @param id_offer  the id of the offer
   * @param id_member the id of the member
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean interestExist(int id_offer, int id_member) {
    return interestDAO.interestExist(id_offer, id_member);
  }

}
