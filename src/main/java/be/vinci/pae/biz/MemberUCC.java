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
}
