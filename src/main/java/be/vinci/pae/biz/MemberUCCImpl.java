package be.vinci.pae.biz;

import be.vinci.pae.dal.MemberDAO;
import jakarta.inject.Inject;
import java.util.List;

public class MemberUCCImpl implements MemberUCC {

  @Inject
  private MemberDAO memberDAO;

  /**
   * Get all the members from the db.
   *
   * @return list of member
   */
  @Override
  public List<MemberDTO> getAllMembers() {
    List<MemberDTO> listMember = memberDAO.getAllMembers();
    return listMember;
  }

  /**
   * Get all the members with the state registered from the db.
   *
   * @return list of member registered
   */
  @Override
  public List<MemberDTO> getMembersRegistered() {
    List<MemberDTO> listMember = memberDAO.getMembersRegistered();
    return listMember;
  }

  /**
   * Get all the members with the state denied from the db.
   *
   * @return list of member denied
   */
  @Override
  public List<MemberDTO> getMembersDenied() {
    List<MemberDTO> listMember = memberDAO.getMembersDenied();
    return listMember;
  }

  /**
   * Get One Member by id.
   *
   * @param id of the member
   * @return Member or null
   */
  @Override
  public MemberDTO getOneMember(int id) {
    return memberDAO.getOneMember(id);
  }

  /**
   * Verify the state of the member and then change the state of the member to confirmed.
   *
   * @param id of the member
   * @return True if success
   */
  @Override
  public boolean confirmMember(int id) {
    Member member = (Member) getOneMember(id);
    if (!member.verifyState("registered") && !member.verifyState("denied")) {
      return false;
    }
    return memberDAO.confirmMember(id);
  }

  /**
   * Verify the state of the member and then change the state of the member to denied.
   *
   * @param id of the member
   * @return True if success
   */
  @Override
  public boolean denyMember(int id) {
    Member member = (Member) getOneMember(id);
    if (!member.verifyState("registered")) {
      return false;
    }
    return memberDAO.denyMember(id);
  }

  /**
   * ONLY FOR MY TESTS
   *
   * @param id test
   * @return test
   */
  public boolean registerTESTMember(int id) {
    return memberDAO.registerTESTMember(id);
  }

  /**
   * Verify the state of the member and then change the state of the member to confirmed and member
   * is an admin.
   *
   * @param id of the member
   * @return True if success
   */
  public boolean confirmAdmin(int id) {
    Member member = (Member) getOneMember(id);
    if (!member.verifyState("registered")) {
      return false;
    }
    memberDAO.confirmMember(id);
    return memberDAO.isAdmin(id);
  }

  /**
   * Get the member from the db, checks its state.
   *
   * @param username of the member
   * @param password of the member
   */
  @Override
  public MemberDTO login(String username, String password) {
    Member member = (Member) memberDAO.getOne(username, password);
    if (member == null || !member.checkPassword(password, member.getPassword())) {
      return null;
    }
    member.verifyState("confirmed");
    return member;
  }
}