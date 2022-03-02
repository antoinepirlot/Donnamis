package be.vinci.pae.biz;

import be.vinci.pae.dal.MemberDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class MemberUCCImpl implements MemberUCC {

  private final MemberDAO memberDAO = new MemberDAO();
  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Override
  public List<MemberDTO> getAll() {
    return memberDAO.getAll();
  }

  @Override
  public String login(String username, String password) {
    Member member = (Member) memberDAO.getOne(username, password);
    member.verifyState();
    return member.createToken();
  }
}