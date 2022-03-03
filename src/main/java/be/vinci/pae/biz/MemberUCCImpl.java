package be.vinci.pae.biz;

import be.vinci.pae.dal.MemberDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class MemberUCCImpl implements MemberUCC {

  private final MemberDAO memberDAO = new MemberDAO();
  private final ObjectMapper jsonMapper = new ObjectMapper();

  /**
   * Get all members from the database.
   *
   * @return all members
   */
  @Override
  public List<MemberDTO> getAll() {
    return memberDAO.getAll();
  }

  /**
   * Get the member from the db, checks its state and return a token for the member if it's
   * allowed.
   *
   * @param username of the member
   * @param password of the member
   * @return a token for the member
   */
  @Override
  public String login(String username, String password) {
    Member member = (Member) memberDAO.getOne(username, password);
    member.verifyState();
    return member.createToken();
  }
}