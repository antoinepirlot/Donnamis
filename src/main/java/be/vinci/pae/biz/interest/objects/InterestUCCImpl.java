package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public boolean markInterest(InterestDTO interestDTO) {
    try {
      dalServices.start();
      boolean isDone = interestDAO.markInterest(interestDTO);
      dalServices.commit();
      return isDone;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean interestExist(InterestDTO interestDTO) {
    try {
      dalServices.start();
      boolean interestExist = this.interestDAO.interestExist(interestDTO);
      dalServices.commit();
      return interestExist;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }
}
