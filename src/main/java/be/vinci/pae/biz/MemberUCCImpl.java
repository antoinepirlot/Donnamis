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

  /**
   * Get the member from the db, checks its state.
   *
   * @param username of the member
   * @param password of the member
   */
  @Override
  public void login(String username, String password) {
    Member member = (Member) memberDAO.getOne(username, password);
    member.verifyState();
  }

}