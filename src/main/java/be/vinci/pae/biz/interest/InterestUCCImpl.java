package be.vinci.pae.biz.interest;

import be.vinci.pae.dal.interest.InterestDAO;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;

  @Override
  public int markInterest(int id_member, int id_offer, boolean call_wanted) {
    LocalDate date = LocalDate.now();
    return interestDAO.markInterest(id_member, id_offer, call_wanted, date);
  }

  @Override
  public boolean offerExist(int id_offer) {
    return interestDAO.offerExist(id_offer);
  }

  @Override
  public boolean memberExist(int id_member) {
    return interestDAO.memberExist(id_member);
  }

}
