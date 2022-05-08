package be.vinci.pae.biz.member.interfaces;

import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import java.util.List;

public interface MemberUCC {

  /**
   * Asks UCC to get a list of all members.
   *
   * @return the list of all members
   */
  List<MemberDTO> getAllMembers();

  /**
   * Asks the UCC to get the member identified by its id.
   *
   * @param idMember the member's id
   * @return the member or null if there's no member with the id
   */
  MemberDTO getOneMember(int idMember);

  /**
   * Get one member by his username.
   *
   * @param memberDTO the member with the username
   * @return the member found or null
   */
  MemberDTO getOneMember(MemberDTO memberDTO);

  /**
   * Modify the member identified by its id.
   *
   * @param memberDTO the new member
   */
  void modifyMember(MemberDTO memberDTO);

  /**
   * Confirm the inscription of a member.
   *
   * @param memberDTO the member to confirm
   */
  void confirmMember(MemberDTO memberDTO);

  /**
   * Switch the state of the member between confirmed and unavailable.
   *
   * @param memberDTO the member to modify state
   */
  void setMemberAvailability(MemberDTO memberDTO);

  /**
   * Verify the state of the member and then change the state of the member to denied.
   *
   * @param refusalDTO the refusal information
   */
  void denyMember(RefusalDTO refusalDTO);

  /**
   * Verify if the member exist in the DB.
   *
   * @param memberDTO the if od the member
   * @return true if exist in the DB false if not
   */
  boolean memberExist(MemberDTO memberDTO, int idMember);

  /**
   * Get the member from the db, checks its state and password.
   *
   * @param memberToLogIn the member who try to log in
   */
  MemberDTO login(MemberDTO memberToLogIn);

  /**
   * Ask DAO to insert the member into the db.
   *
   * @param memberDTO member to add in the db
   */
  void register(MemberDTO memberDTO);

  /**
   * Get the list of interested member of the item and returns it.
   *
   * @param idOffer the id of the item
   * @return the list of interested members
   */
  List<MemberDTO> getInterestedMembers(int idOffer);
}
