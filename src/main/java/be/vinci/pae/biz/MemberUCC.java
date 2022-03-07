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
   * Get member from the db and check its state.
   *
   * @param username whose information we want
   * @return memberDTO from the username in parameter
   */
  MemberDTO getMember(String username, int id);
}
