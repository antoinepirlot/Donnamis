package be.vinci.pae.biz;

import java.util.List;

public interface MemberUCC {

  /**
   * Get all the members from the db.
   *
   * @return list of member
   */
  List<MemberDTO> getAllMembers();

  /**
   * Get all the members with the state registered from the db.
   *
   * @return list of member registered
   */
  List<MemberDTO> getMembersRegistered();

  /**
   * Get all the members with the state denied from the db.
   *
   * @return list of member denied
   */
  List<MemberDTO> getMembersDenied();

  /**
   * Get the member from the db, checks its state and password.
   *
   * @param username of the member
   * @param password of the member
   */
  MemberDTO login(String username, String password);
}
