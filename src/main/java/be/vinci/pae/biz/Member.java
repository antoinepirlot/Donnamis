package be.vinci.pae.biz;

public interface Member extends MemberDTO {

  /**
   * Verify if the state of the member is allowed to connect to the website. If the state is
   * "confirmed" the user can access.
   *
   * @return true if the actual state is "confirmed", else return false
   */
  boolean verifyState();

  boolean checkPassword(String password, String hashedPassword);

  void hashPassword();
}
