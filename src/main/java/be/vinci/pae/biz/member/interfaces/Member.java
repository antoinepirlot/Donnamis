package be.vinci.pae.biz.member.interfaces;

public interface Member extends MemberDTO {

  /**
   * Verify if the state of the member is allowed to connect to the website. If the state is
   * "confirmed" the user can access.
   *
   * @return true if the actual state is "confirmed", else return false
   */
  boolean verifyState(String expectedState);

  /**
   * Check if the password is correct.
   * @param password the password to check
   * @param hashedPassword the good password (hashed)
   * @return true if the password is correct, otherwise false
   */
  boolean checkPassword(String password, String hashedPassword);

  /**
   * Crypt the password with BCrypt.
   */
  void hashPassword();
}
