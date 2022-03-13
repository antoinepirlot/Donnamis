package be.vinci.pae.dal;

import be.vinci.pae.biz.MemberDTO;
import java.util.List;

public interface MemberDAO {

  List<MemberDTO> getAllMembers();

  List<MemberDTO> getMembersRegistered();

  List<MemberDTO> getMembersDenied();

  MemberDTO getOneMember(int id);

  boolean confirmMember(int id);

  boolean isAdmin(int id);

  boolean registerTESTMember(int id);

  boolean denyMember(int id);

  MemberDTO getOne(String username, String password);
}
