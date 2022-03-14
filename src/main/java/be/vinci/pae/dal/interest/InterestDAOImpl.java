package be.vinci.pae.dal.interest;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.dal.DALServices;
import jakarta.inject.Inject;

public class InterestDAOImpl implements InterestDAO {

  @Inject
  private DALServices dalServices;
  @Inject
  private Factory factory;

  @Override
  public void markInterest(int id) {

  }
}
