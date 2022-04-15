package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public boolean markInterest(InterestDTO interestDTO)
      throws SQLException {
    dalServices.start();
    boolean isDone;
    try {
      isDone = interestDAO.markInterest(interestDTO);
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
    dalServices.commit();
    return isDone;
  }

  @Override
  public boolean interestExist(InterestDTO interestDTO) throws SQLException {
    dalServices.start();
    boolean interestExist = false;
    try {
      interestExist = this.interestDAO.interestExist(interestDTO);
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
    dalServices.commit();
    return interestExist;
  }
}
