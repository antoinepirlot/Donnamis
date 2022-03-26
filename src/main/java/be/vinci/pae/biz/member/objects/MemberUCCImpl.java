package be.vinci.pae.biz.member.objects;

import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import jakarta.inject.Inject;
import java.util.List;

public class MemberUCCImpl implements MemberUCC {

  @Inject
  private MemberDAO memberDAO;


  @Override
  public List<MemberDTO> getAllMembers() {
    return memberDAO.getAllMembers();
  }


  @Override
  public List<MemberDTO> getMembersRegistered() {
    return memberDAO.getMembersRegistered();
  }

  @Override
  public List<MemberDTO> getMembersDenied() {
    return memberDAO.getMembersDenied();
  }

  @Override
  public MemberDTO getOneMember(int id) {
    return memberDAO.getOneMember(id);
  }

  @Override
  public MemberDTO confirmMember(int id) {
    Member member = (Member) getOneMember(id);
    if (!member.verifyState("registered") && !member.verifyState("denied")) {
      return null;
    }
    return memberDAO.confirmMember(id);
  }

  @Override
  public MemberDTO denyMember(int id) {
    Member member = (Member) getOneMember(id);
    if (!member.verifyState("registered")) {
      return null;
    }
    return memberDAO.denyMember(id);
  }

  public MemberDTO confirmAdmin(int id) {
    Member member = (Member) getOneMember(id);
    if (!member.verifyState("registered") && !member.verifyState("denied")) {
      return null;
    }
    memberDAO.confirmMember(id);
    return memberDAO.isAdmin(id);
  }

  @Override
  public MemberDTO login(MemberDTO memberToLogIn) {
    Member memberToLogin = (Member) memberToLogIn;
    Member loggedMember = (Member) memberDAO.getOne(memberToLogin);
    if (
        loggedMember == null
            || !loggedMember.checkPassword(memberToLogin.getPassword(), loggedMember.getPassword())
            || !loggedMember.verifyState("confirmed")
    ) {
      return null;
    }
    return loggedMember;
  }

  @Override
  public boolean register(MemberDTO memberDTO) {
    Member member = (Member) memberDTO;
    member.hashPassword();
    return this.memberDAO.register(member);
  }
}