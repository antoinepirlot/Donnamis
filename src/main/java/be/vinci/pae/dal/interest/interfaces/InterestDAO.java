package be.vinci.pae.dal.interest.interfaces;

import java.time.LocalDate;

public interface InterestDAO {

  int markInterest(int idMember, int idOffer, boolean callWanted, LocalDate date);

  boolean offerExist(int idOffer);

  boolean memberExist(int idMember);

  boolean interestExist(int idOffer, int idMember);

}
