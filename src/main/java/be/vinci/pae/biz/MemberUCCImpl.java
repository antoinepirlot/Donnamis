package be.vinci.pae.biz;

import be.vinci.pae.dal.MemberDAO;
import jakarta.inject.Inject;
import java.util.List;

public class MemberUCCImpl implements MemberUCC {

  @Inject
  private MemberDAO memberDAO;

  //  /**
  //   * Get all members from the database.
  //   *
  //   * @return all members
  //   */
  //  @Override
  //  public List<MemberDTO> getAll() {
  //    return memberDAO.getAll();
  //  }

  @Override
  public List<MemberDTO> getAllMembers() {
    List<MemberDTO> listMember = memberDAO.getAllMembers();
    return listMember;
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
    if (
        member == null
            || !member.checkPassword(password, member.getPassword())
            || !member.verifyState()
    ) {
      return null;
    }
    return member;
  }

  /**
   * @param username  of the member
   * @param password  of the member
   * @param firstName of the member
   * @param lastName  of the member
   * @return true if the member has been  registered
   */
  @Override
  public boolean register(String username, String password, String firstName, String lastName) {
    return this.memberDAO.register(username, password, firstName, lastName);
  }
}