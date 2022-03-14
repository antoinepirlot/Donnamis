package be.vinci.pae.biz.interest;

import be.vinci.pae.dal.interest.InterestDAO;
import jakarta.inject.Inject;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;

  @Override
  public void markInterest(int id) {

  }

}
