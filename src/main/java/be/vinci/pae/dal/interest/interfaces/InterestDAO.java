package be.vinci.pae.dal.interest.interfaces;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import java.sql.SQLException;

public interface InterestDAO {

  boolean markInterest(InterestDTO interestDTO)
      throws SQLException;

  boolean interestExist(InterestDTO interestDTO) throws SQLException;

}
