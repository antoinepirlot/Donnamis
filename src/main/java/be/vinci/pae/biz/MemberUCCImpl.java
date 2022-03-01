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
  public Member login(String username, String password) {
    return (Member) memberDAO.getOne(username, password);
  }

  public String getToken(Member member) {
    return member.createToken();
  }
}