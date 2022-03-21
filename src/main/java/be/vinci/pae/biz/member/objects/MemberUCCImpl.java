package be.vinci.pae.biz.member.objects;

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

  /**
   * Get all the members from the db.
   *
   * @return list of member
   */
  @Override
  public List<MemberDTO> getAllMembers() {
    dalServices.start();
    List<MemberDTO> memberDTOList = memberDAO.getAllMembers();
    if (memberDTOList == null) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
    return memberDTOList;
  }

  /**
   * Get all the members with the state registered from the db.
   *
   * @return list of member registered
   */
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

  /**
   * Get all the members with the state denied from the db.
   *
   * @return list of member denied
   */
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

  /**
   * Get One Member by id.
   *
   * @param id of the member
   * @return Member or null
   */
  @Override
  public MemberDTO getOneMember(int id) {
    dalServices.start();
    MemberDTO memberDTO = memberDAO.getOneMember(id);
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
  @Override
  public MemberDTO confirmMember(int id) {
    dalServices.start();
    Member member = (Member) getOneMember(id);
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

  /**
   * Verify the state of the member and then change the state of the member to denied.
   *
   * @param id of the member
   * @return True if success
   */
  @Override
  public MemberDTO denyMember(int id) {
    dalServices.start();
    Member member = (Member) getOneMember(id);
    if (!member.verifyState("registered")) {
      dalServices.rollback();
      return null;
    }
    MemberDTO memberDTO = memberDAO.denyMember(id);
    if (memberDTO == null) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
    return memberDTO;
  }

  /**
   * Verify the state of the member and then change the state of the member to confirmed and member
   * is an admin.
   *
   * @param id of the member
   * @return True if success
   */
  public MemberDTO confirmAdmin(int id) {
    dalServices.start();
    Member member = (Member) getOneMember(id);
    if (!member.verifyState("registered") && !member.verifyState("denied")) {
      dalServices.rollback();
      return null;
    }
    MemberDTO memberDTO = memberDAO.confirmMember(id);
    if (memberDTO == null) {
      dalServices.rollback();
      return null;
    }
    memberDTO = memberDAO.confirmMember(id);
    if (memberDTO == null) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
    return memberDTO;
  }

  /**
   * Get the member from the db, checks its state.
   *
   * @param username of the member
   * @param password of the member
   */
  @Override
  public MemberDTO login(String username, String password) {
    dalServices.start();
    Member member = (Member) memberDAO.getOne(username);
    if (
        member == null
            || !member.checkPassword(password, member.getPassword())
            || !member.verifyState("confirmed")
    ) {
      dalServices.rollback();
      return null;
    }
    dalServices.commit();
    return member;
  }

  /**
   * Ask DAO to insert the member into the db.
   *
   * @param memberDTO member to add in the db
   * @return true if the member has been  registered
   */
  @Override
  public boolean register(MemberDTO memberDTO) {
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