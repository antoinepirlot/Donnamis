package be.vinci.pae.biz.refusal.interfaces;

public interface RefusalUCC {

  /**
   * Get the refusal message of a member.
   *
   * @param username the member's username
   * @return the refusal objet that contains the message
   */
  RefusalDTO getRefusal(String username);
}
