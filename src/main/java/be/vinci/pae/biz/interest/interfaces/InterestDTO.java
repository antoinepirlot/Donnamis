package be.vinci.pae.biz.interest.interfaces;

import java.util.Date;

public interface InterestDTO {

  int getId_interest();

  void setId_interest(int id_interest);

  boolean isCall_wanted();

  void setCall_wanted(boolean call_wanted);

  int getId_offer();

  void setId_offer(int id_offer);

  int getId_member();

  void setId_member(int id_member);

  Date getDate();

  void setDate(Date date);
}
