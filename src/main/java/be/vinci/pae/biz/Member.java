package be.vinci.pae.biz;

import jakarta.ws.rs.WebApplicationException;

public interface Member extends MemberDTO {

  /**
   * Verify if the state of the member is allowed to connect to the website. If the state is
   * "confirmed" the user can access.
   *
   * @throws WebApplicationException if the state is "registered" or "denied"
   */
  void verifyState();
}
