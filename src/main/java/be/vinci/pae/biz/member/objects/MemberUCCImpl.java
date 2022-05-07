package be.vinci.pae.biz.member.objects;

import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class MemberUCCImpl implements MemberUCC {

  @Inject
  private MemberDAO memberDAO;
  @Inject
  private DALServices dalServices;


  @Override
  public List<MemberDTO> getAllMembers() {
    try {
      dalServices.start();
      List<MemberDTO> memberDTOList = memberDAO.getAllMembers();
      dalServices.commit();
      return memberDTOList;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public MemberDTO getOneMember(int id) {
    try {
      dalServices.start();
      MemberDTO memberDTO = memberDAO.getOne(id);
      dalServices.commit();
      return memberDTO;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public MemberDTO getOneMember(MemberDTO memberDTO) {
    try {
      dalServices.start();
      MemberDTO memberDTOFound = memberDAO.getOne(memberDTO);
      dalServices.commit();
      return memberDTOFound;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean modifyMember(MemberDTO memberDTO) {
    Member member = (Member) memberDTO;
    member.hashPassword();
    try {
      dalServices.start();
      boolean modified = memberDAO.modifyMember(memberDTO);
      dalServices.commit();
      return modified;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean confirmMember(MemberDTO memberDTO) {
    try {
      dalServices.start();
      boolean isConfirmed = memberDAO.confirmMember(memberDTO);
      dalServices.commit();
      return isConfirmed;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean setMemberAvailability(MemberDTO memberDTO) {
    try {
      dalServices.start();
      boolean isChange = memberDAO.setMemberAvailability(memberDTO);
      dalServices.commit();
      return isChange;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean denyMember(RefusalDTO refusalDTO) {
    try {
      dalServices.start();
      boolean isDenied = memberDAO.denyMember(refusalDTO);
      dalServices.commit();
      return isDenied;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO, int idMember) {
    try {
      dalServices.start();
      boolean doesExist = this.memberDAO.memberExist(memberDTO, idMember);
      dalServices.commit();
      return doesExist;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public MemberDTO login(MemberDTO memberToLogIn) {
    Member memberToLogin = (Member) memberToLogIn;
    try {
      dalServices.start();
      Member loggedMember = (Member) memberDAO.getOne(memberToLogin);
      dalServices.commit();
      if (
          loggedMember == null
              || !loggedMember.checkPassword(memberToLogin.getPassword(),
              loggedMember.getPassword())
              || !loggedMember.verifyState("confirmed") && !loggedMember.verifyState(
              "unavailable")
      ) {
        return null;
      }
      return loggedMember;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean register(MemberDTO memberDTO) {
    Member member = (Member) memberDTO;
    member.hashPassword();
    try {
      dalServices.start();
      boolean isRegistered = memberDAO.register(member);
      dalServices.commit();
      return isRegistered;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<MemberDTO> getInterestedMembers(int idOffer) {
    try {
      this.dalServices.start();
      List<MemberDTO> memberDTOList = this.memberDAO.getInterestedMembers(idOffer);
      this.dalServices.commit();
      return memberDTOList;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

}