package be.vinci.pae.dal.interest.interfaces;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;

public interface InterestDAO {

  boolean markInterest(InterestDTO interestDTO);

  boolean interestExist(InterestDTO interestDTO);

}
