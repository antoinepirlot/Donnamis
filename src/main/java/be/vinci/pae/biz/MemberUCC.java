package be.vinci.pae.biz;

import java.util.List;

public interface MemberUCC {

  String login(String username, String password);

  List<MemberDTO> getAll();
}
