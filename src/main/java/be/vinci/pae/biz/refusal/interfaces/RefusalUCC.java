package be.vinci.pae.biz.refusal.interfaces;

import java.sql.SQLException;

public interface RefusalUCC {

  /**
   * Get the refusal message of a member.
   *
   * @param username the member's username
   * @return the refusal objet that contains the message
   * @throws SQLException if an error occurs while getting the refusal information
   */
  RefusalDTO getRefusal(String username) throws SQLException;
}
