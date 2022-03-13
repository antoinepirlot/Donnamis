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
   * Get One Member by id.
   *
   * @return Member or null
   */
  MemberDTO getOneMember(int id);

  /**
   * Verify the state of the member and then change the state of the member.
   *
   * @param id of the member
   * @return True if success
   */
  boolean confirmMember(int id);

  /**
   * Verify the state of the member and then change the state of the member to denied.
   *
   * @param id of the member
   * @return True if success
   */
  boolean denyMember(int id);

  /**
   * ONLY FOR MY TESTS
   *
   * @param id
   * @return
   */
  boolean registerTESTMember(int id);

  boolean confirmAdmin(int id);

  /**
   * Get the member from the db, checks its state and password.
   *
   * @param username of the member
   * @param password of the member
   */
  MemberDTO login(String username, String password);
}
