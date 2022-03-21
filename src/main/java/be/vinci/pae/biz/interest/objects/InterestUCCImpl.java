package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;
  @Inject
  private DALServices dalServices;

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
    dalServices.start();
    LocalDate date = LocalDate.now();
    int res = interestDAO.markInterest(idMember, idOffer, callWanted, date);
    if (res == -1) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
    return res;
  }

  /**
   * Verify if the offer exist in the DB.
   *
   * @param idOffer the if of the offer
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean offerExist(int idOffer) {
    dalServices.start();
    if (!interestDAO.offerExist(idOffer)) {
      dalServices.rollback();
      return false;
    }
    dalServices.commit();
    return true;
  }

  /**
   * Verify if the member exist in the DB.
   *
   * @param idMember the if od the member
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean memberExist(int idMember) {
    dalServices.start();
    if (!interestDAO.memberExist(idMember)) {
      dalServices.rollback();
    }
    dalServices.commit();
    return true;
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
    dalServices.start();
    if (!interestDAO.interestExist(idOffer, idMember)) {
      dalServices.rollback();
    }
    dalServices.commit();
    return true;
  }

}
