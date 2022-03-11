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
   * Change the state of the member.
   *
   * @param id of the member
   * @return Member or null
   */
  @Override
  public boolean confirmRegistration(int id) {
    return memberDAO.confirmRegistration(id);
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
    member.verifyState();
    return member;
  }
}