package be.vinci.pae.biz;

public interface MemberUCC {

  //  /**
  //   * Get all members from the database.
  //   *
  //   * @return all members
  //   */
  //  List<MemberDTO> getAll();

  /**
   * Get the member from the db, checks its state and password.
   *
   * @param username of the member
   * @param password of the member
   */
  MemberDTO login(String username, String password);

  /**
   * Get member from the db and check its state.
   *
   * @param username
   * @return memberDTO from the username in parameter
   */
  MemberDTO getMember(String username);
}
