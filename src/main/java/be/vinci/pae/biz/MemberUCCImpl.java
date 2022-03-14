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
    return memberDAO.getAllMembers();
  }

  /**
   * Get the member from the db, checks its state.
   *
   * @param username of the member
   * @param password of the member
   */
  @Override
  public MemberDTO login(String username, String password) {
    Member member = (Member) memberDAO.getOne(username);
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
   * Ask DAO to insert the member into the db.
   * @param memberDTO member to add in the db
   * @return true if the member has been  registered
   */
  @Override
  public boolean register(MemberDTO memberDTO) {
    Member member = (Member) memberDTO;
    member.hashPassword();
    return this.memberDAO.register(member);
  }
}