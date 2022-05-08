package be.vinci.pae.biz.member.objects;

import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ForbiddenException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
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
      if (memberDTOList == null) {
        throw new ObjectNotFoundException("No member into the database");
      }
      return memberDTOList;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public MemberDTO getOneMember(int idMember) {
    if (!this.memberExist(null, idMember)) {
      String message = "This member has not been found. Member's id: " + idMember;
      throw new ObjectNotFoundException(message);
    }
    try {
      dalServices.start();
      MemberDTO memberDTO = memberDAO.getOne(idMember);
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
  public void modifyMember(MemberDTO memberDTO) {
    this.checkVersion(memberDTO);
    Member member = (Member) memberDTO;
    member.hashPassword();
    try {
      dalServices.start();
      boolean modified = memberDAO.modifyMember(memberDTO);
      dalServices.commit();
      if (!modified) {
        throw new FatalException("An error occured while modifying member");
      }
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public void confirmMember(MemberDTO memberDTO) {
    this.checkVersion(memberDTO);
    try {
      dalServices.start();
      boolean isConfirmed = memberDAO.confirmMember(memberDTO);
      dalServices.commit();
      if (!isConfirmed) {
        throw new FatalException("An unexpected error happened while confirming member.");
      }
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public void setMemberAvailability(MemberDTO memberDTO) {
    this.checkVersion(memberDTO);
    try {
      dalServices.start();
      boolean isChanged = memberDAO.setMemberAvailability(memberDTO);
      dalServices.commit();
      if (!isChanged) {
        throw new FatalException("An unexpected error happened while set member availability.");
      }
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public void denyMember(RefusalDTO refusalDTO) {
    try {
      dalServices.start();
      boolean isDenied = memberDAO.denyMember(refusalDTO);
      dalServices.commit();
      if (!isDenied) {
        throw new FatalException("An unexpected error happened while denying member.");
      }
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
        if (loggedMember != null && (loggedMember.verifyState("registered")
            || loggedMember.verifyState("denied"))
            && loggedMember.checkPassword(memberToLogin.getPassword(),
            loggedMember.getPassword())) {
          throw new ForbiddenException("Username and password ok but registered");
        }
        throw new ObjectNotFoundException("Member doesn't exist");
      }
      return loggedMember;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public void register(MemberDTO memberDTO) {
    if (this.memberExist(memberDTO, -1)) {
      String message = "This member already exist in the database";
      throw new ConflictException(message);
    }
    Member member = (Member) memberDTO;
    member.hashPassword();
    try {
      dalServices.start();
      boolean isRegistered = memberDAO.register(member);
      dalServices.commit();
      if (!isRegistered) {
        throw new FatalException("An error occurs while registering");
      }
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

  private void checkVersion(MemberDTO member) {
    Member dbMember = (Member) this.getOneMember(member.getId());
    if (member.getVersion() != dbMember.getVersion()) {
      throw new FatalException("Error with version");
    }
  }

}