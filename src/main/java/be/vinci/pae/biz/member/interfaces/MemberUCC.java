package be.vinci.pae.biz.member.interfaces;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import java.util.List;

public interface MemberUCC {

  /**
   * Asks UCC to get a list of all members.
   *
   * @return the list of all members
   */
  List<MemberDTO> getAllMembers();

  /**
   * Asks UCC to get a list of registered members.
   *
   * @return the list of registered members
   */
  List<MemberDTO> getMembersRegistered();

  /**
   * Asks UCC to get the list of denied members.
   *
   * @return the list of denied members
   */
  List<MemberDTO> getMembersDenied();

  /**
   * Asks the UCC to get the member identified by its id.
   *
   * @param id the member's id
   * @return the member or null if there's no member with the id
   */
  MemberDTO getOneMember(int id);

  /**
   * Asks the UCC to get the adress of the membe identified by its id.
   *
   * @param id the member's id
   * @return the adress or null if there's no member with the id
   */
  AddressDTO getAddressMember(int id);

  /**
   * Verify the state of the member and then change the state of the member.
   *
   * @param id of the member
   * @return True if success
   */
  MemberDTO confirmMember(int id);

  /**
   * Verify the state of the member and then change the state of the member to denied.
   *
   * @param id of the member
   * @return True if success
   */
  MemberDTO denyMember(int id, String refusalText);

  /**
   * Verify the state of the member and then change the state of the member to confirmed.
   *
   * @param id of the member
   * @return True if success
   */
  MemberDTO confirmAdmin(int id);

  /**
   * Verify if the member exist in the DB.
   *
   * @param memberDTO the if od the member
   * @return true if exist in the DB false if not
   */
  boolean memberExist(MemberDTO memberDTO);

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
}
