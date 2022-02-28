package be.vinci.pae.biz;

import be.vinci.pae.dal.MemberDAO;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MemberUCC {

  private final MemberDAO memberDAO;

  public MemberUCC() {
    this.memberDAO = new MemberDAO();
  }

  public ObjectNode login(String username, String password) {
    return memberDAO.login(username, password);
  }
}
