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
    try {
      dalServices.start();
      List<MemberDTO> memberDTOList = memberDAO.getAllMembers();
      dalServices.commit();
      return memberDTOList;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public MemberDTO getOneMember(int id) throws SQLException {
    try {
      dalServices.start();
      MemberDTO memberDTO = memberDAO.getOne(id);
      dalServices.commit();
      return memberDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public AddressDTO getAddressMember(int id) throws SQLException {
    try {
      dalServices.start();
      AddressDTO addressDTO = memberDAO.getAddressMember(id);
      dalServices.commit();
      return addressDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public MemberDTO modifyMember(int id, MemberDTO memberDTO) throws SQLException {
    Member member = (Member) memberDTO;
    member.hashPassword();
    try {
      dalServices.start();
      MemberDTO modifyMember = memberDAO.modifyMember(id, memberDTO);
      dalServices.commit();
      return modifyMember;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public boolean confirmMember(MemberDTO memberDTO) throws SQLException {
    try {
      dalServices.start();
      boolean isConfirmed = memberDAO.confirmMember(memberDTO);
      dalServices.commit();
      return isConfirmed;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public boolean denyMember(RefusalDTO refusalDTO) throws SQLException {
    try {
      dalServices.start();
      boolean isDenied = memberDAO.denyMember(refusalDTO);
      dalServices.commit();
      return isDenied;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO, int idMember) throws SQLException {
    try {
      dalServices.start();
      boolean doesExist = this.memberDAO.memberExist(memberDTO, idMember);
      dalServices.commit();
      return doesExist;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public MemberDTO login(MemberDTO memberToLogIn) throws SQLException {
    Member memberToLogin = (Member) memberToLogIn;
    try {
      dalServices.start();
      Member loggedMember = (Member) memberDAO.getOne(memberToLogin);
      dalServices.commit();
      if (
          loggedMember == null
              || !loggedMember.checkPassword(memberToLogin.getPassword(),
              loggedMember.getPassword())
              || !loggedMember.verifyState("confirmed")
      ) {
        return null;
      }
      return loggedMember;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public boolean register(MemberDTO memberDTO) throws SQLException {
    Member member = (Member) memberDTO;
    member.hashPassword();
    try {
      dalServices.start();
      boolean isRegistered = memberDAO.register(member);
      dalServices.commit();
      return isRegistered;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }
}