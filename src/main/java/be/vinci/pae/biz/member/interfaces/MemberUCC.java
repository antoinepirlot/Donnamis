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
   * @param id the member's id
   * @return the member or null if there's no member with the id
   */
  MemberDTO getOneMember(int id);

  /**
   * Modify the member identified by its id.
   *
   * @param memberDTO the new member
   * @return the member or null if there's no member with the id
   */
  MemberDTO modifyMember(MemberDTO memberDTO);

  /**
   * Confirm the inscription of a member.
   *
   * @param memberDTO the member to confirm
   * @return True if success
   */
  boolean confirmMember(MemberDTO memberDTO);

  /**
   * Switch the state of the member between confirmed and unavailable.
   *
   * @param memberDTO the member to modify state
   * @return True if success
   */
  boolean setMemberAvailability(MemberDTO memberDTO);

  /**
   * Verify the state of the member and then change the state of the member to denied.
   *
   * @param refusalDTO the refusal information
   * @return true if deny is complete, otherwise false
   */
  boolean denyMember(RefusalDTO refusalDTO);

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
   * @return true if the member has been  registered
   */
  boolean register(MemberDTO memberDTO);

  /**
   * Get the list of interested member of the item and returns it.
   *
   * @param idOffer the id of the item
   * @return the list of interested members
   */
  List<MemberDTO> getInterestedMembers(int idOffer);
}
