package be.vinci.pae.biz.member.objects;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class MemberUCCImpl implements MemberUCC {

  @Inject
  private MemberDAO memberDAO;
  @Inject
  private DALServices dalServices;


  @Override
  public List<MemberDTO> getAllMembers() throws SQLException {
    dalServices.start();
    List<MemberDTO> memberDTOList = memberDAO.getAllMembers();
    if (memberDTOList == null) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return memberDTOList;
  }

  @Override
  public MemberDTO getOneMember(int id) throws SQLException {
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
  public AddressDTO getAddressMember(int id) throws SQLException {
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
  public MemberDTO modifyMember(int id, MemberDTO memberDTO) throws SQLException {
    dalServices.start();
    MemberDTO modifyMember = memberDAO.modifyMember(id, memberDTO);
    if (modifyMember == null) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return modifyMember;
  }

  @Override
  public boolean confirmMember(MemberDTO memberDTO) throws SQLException {
    dalServices.start();
    Member member = (Member) memberDAO.getOneMember(memberDTO.getId());
    if (member == null) {
      dalServices.rollback();
      return false;
    }
    memberDAO.confirmMember(memberDTO);
    dalServices.commit();
    return true;
  }

  @Override
  public boolean denyMember(RefusalDTO refusalDTO) throws SQLException {
    dalServices.start();
    try {
      memberDAO.denyMember(refusalDTO);
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
    dalServices.commit();
    return true;
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO, int idMember) throws SQLException {
    dalServices.start();
    if (!this.memberDAO.memberExist(memberDTO, idMember)) {
      dalServices.rollback();
      return false;
    }
    dalServices.commit();
    return true;
  }

  @Override
  public MemberDTO login(MemberDTO memberToLogIn) throws SQLException {
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
  public boolean register(MemberDTO memberDTO) throws SQLException {
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