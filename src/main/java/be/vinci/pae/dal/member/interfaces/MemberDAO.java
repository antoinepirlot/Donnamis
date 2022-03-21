package be.vinci.pae.dal.member.interfaces;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import java.util.List;

public interface MemberDAO {

  List<MemberDTO> getAllMembers();

  MemberDTO getOne(String username);

  /**
   * Add a new member to the db if it's not already in the db.
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been  registered
   */
  boolean register(MemberDTO memberDTO);

  List<MemberDTO> getMembersRegistered();

  List<MemberDTO> getMembersDenied();

  MemberDTO getOneMember(int id);

  MemberDTO confirmMember(int id);

  MemberDTO isAdmin(int id);

  MemberDTO denyMember(int id);
}
