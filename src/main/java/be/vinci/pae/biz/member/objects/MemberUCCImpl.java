package be.vinci.pae.biz.member.objects;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.util.List;

public class MemberUCCImpl implements MemberUCC {

  @Inject
  private MemberDAO memberDAO;
  @Inject
  private DALServices dalServices;


  @Override
  public List<MemberDTO> getAllMembers() {
    dalServices.start();
    List<MemberDTO> memberDTOList = memberDAO.getAllMembers();
    if (memberDTOList == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return memberDTOList;
  }


  @Override
  public List<MemberDTO> getMembersRegistered() {
    dalServices.start();
    List<MemberDTO> listMember = memberDAO.getMembersRegistered();
    if (listMember == null) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
    return listMember;
  }

  @Override
  public List<MemberDTO> getMembersDenied() {
    dalServices.start();
    List<MemberDTO> listMember = memberDAO.getMembersDenied();
    if (listMember == null) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
    return listMember;
  }

  @Override
  public MemberDTO getOneMember(int id) {
    dalServices.start();
    MemberDTO memberDTO = memberDAO.getOneMember(id);
    if (memberDTO == null) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return memberDTO;
  }

  @Override
  public AddressDTO getAddressMember(int id) {
    dalServices.start();
    AddressDTO addressDTO = memberDAO.getAddressMember(id);
    if (addressDTO == null) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return addressDTO;
  }

  @Override
  public MemberDTO confirmMember(int id) {
    dalServices.start();
    Member member = (Member) memberDAO.getOneMember(id);
    if (member == null) {
      dalServices.rollback();
      return null;
    }
    if (!member.verifyState("registered") && !member.verifyState("denied")) {
      dalServices.rollback();
      return null;
    }
    MemberDTO memberDTO = memberDAO.confirmMember(id);
    if (memberDTO == null) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
    return memberDTO;
  }

  @Override
  public MemberDTO denyMember(int id, String refusalText) {
    dalServices.start();
    Member member = (Member) memberDAO.getOneMember(id);
    if (member == null) {
      dalServices.rollback();
      return null;
    }
    if (!member.verifyState("registered")) {
      dalServices.rollback();
      return null;
    }
    MemberDTO memberDTO = memberDAO.denyMember(id, refusalText);
    if (memberDTO == null) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
    return memberDTO;
  }

  /**
   * Verify the state of the member and then change the state of the member to confirmed.
   *
   * @param id of the member
   * @return True if success
   */
  public MemberDTO confirmAdmin(int id) {
    dalServices.start();
    Member member = (Member) memberDAO.getOneMember(id);
    if (member == null) {
      dalServices.rollback();
      return null;
    }
    if (!member.verifyState("registered") && !member.verifyState("denied")) {
      dalServices.rollback();
      return null;
    }
    MemberDTO memberDTO = memberDAO.confirmMember(id);
    if (memberDTO == null) {
      dalServices.rollback();
      return null;
    }
    memberDTO = memberDAO.isAdmin(id);
    if (memberDTO == null) {
      dalServices.rollback();
    }
    dalServices.commit();
    return memberDTO;
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO) {
    dalServices.start();
    if (!this.memberDAO.memberExist(memberDTO)) {
      dalServices.rollback();
      return false;
    }
    dalServices.commit();
    return true;
  }

  @Override
  public MemberDTO login(MemberDTO memberToLogIn) {
    dalServices.start();
    Member memberToLogin = (Member) memberToLogIn;
    Member loggedMember = (Member) memberDAO.getOne(memberToLogin);
    if (
        loggedMember == null
            || !loggedMember.checkPassword(memberToLogin.getPassword(), loggedMember.getPassword())
            || !loggedMember.verifyState("confirmed")
    ) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return loggedMember;
  }

  @Override
  public boolean register(MemberDTO memberDTO) {
    dalServices.start();
    Member member = (Member) memberDTO;
    member.hashPassword();
    if (!memberDAO.register(member)) {
      dalServices.rollback();
      return false;
    } else {
      dalServices.commit();
      return true;
    }
  }
}