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
  MemberDTO confirmMember(int id);

  /**
   * Verify the state of the member and then change the state of the member to denied.
   *
   * @param id of the member
   * @return True if success
   */
  MemberDTO denyMember(int id);

  /**
   * ONLY FOR MY TESTS
   *
   * @param id
   * @return
   */
  MemberDTO registerTESTMember(int id);

  MemberDTO confirmAdmin(int id);

  /**
   * Get the member from the db, checks its state and password.
   *
   * @param username of the member
   * @param password of the member
   */
  MemberDTO login(String username, String password);

  /**
   * Ask DAO to insert the member into the db.
   * @param memberDTO member to add in the db
   * @return true if the member has been  registered
   */
  boolean register(MemberDTO memberDTO);
}
