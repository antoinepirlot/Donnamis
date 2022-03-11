package be.vinci.pae.biz;

import java.util.List;

public interface MemberUCC {

  //  /**
  //   * Get all members from the database.
  //   *
  //   * @return all members
  //   */
  //  List<MemberDTO> getAll();

  /**
   * Get all the members from the db.
   *
   * @return list of member
   */
  List<MemberDTO> getAllMembers();

  /**
   * Get the member from the db, checks its state and password.
   *
   * @param username of the member
   * @param password of the member
   */
  MemberDTO login(String username, String password);

  /**
   *
   * @param username of the member
   * @param password of the member
   * @param firstName of the member
   * @param lastName of the member
   * @return true if the member has been  registered
   */
  boolean register(String username, String password, String firstName, String lastName);
}
