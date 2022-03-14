package be.vinci.pae.dal;

import be.vinci.pae.biz.MemberDTO;
import java.util.List;

public interface MemberDAO {

  List<MemberDTO> getAllMembers();

  List<MemberDTO> getMembersRegistered();

  List<MemberDTO> getMembersDenied();

  MemberDTO getOneMember(int id);

  MemberDTO confirmMember(int id);

  MemberDTO isAdmin(int id);

  MemberDTO registerTESTMember(int id);

  MemberDTO denyMember(int id);

  MemberDTO getOne(String username, String password);
}
