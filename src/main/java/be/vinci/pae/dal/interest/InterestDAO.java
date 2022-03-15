package be.vinci.pae.dal.interest;

import java.time.LocalDate;

public interface InterestDAO {

  int markInterest(int id_member, int id_offer, boolean call_wanted, LocalDate date);

  boolean offerExist(int id_offer);

  boolean memberExist(int id_member);

  boolean interestExist(int id_offer, int id_member);

}
