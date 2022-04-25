package be.vinci.pae.dal.refusal.interfaces;

import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;

public interface RefusalDAO {

  /**
   * Get the refusal information from the database.
   *
   * @param username the member's username
   * @return the refusal object that contains the refusal information
   */
  RefusalDTO getRefusal(String username);
}
