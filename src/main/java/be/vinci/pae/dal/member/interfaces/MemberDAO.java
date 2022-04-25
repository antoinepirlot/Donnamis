package be.vinci.pae.dal.member.interfaces;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import java.util.List;

public interface MemberDAO {

  List<MemberDTO> getAllMembers();

  /**
   * Get one member with member information, use the username.
   *
   * @param memberDTO the member to get
   * @return the member
   */
  MemberDTO getOne(MemberDTO memberDTO);

  /**
   * Get one member identified by its id.
   *
   * @param id the member's id
   * @return the found member
   */
  MemberDTO getOne(int id);

  /**
   * Add a new member to the db if it's not already in the db.
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been  registered
   */
  boolean register(MemberDTO memberDTO);

  /**
   * Confirm the member.
   *
   * @param memberDTO the member to confirm
   * @return true if the member has been confirmed, otherwise false
   */
  boolean confirmMember(MemberDTO memberDTO);

  /**
   * Checks if the member is admin.
   *
   * @param id the member's id
   * @return the member if the member is admin otherwise null
   */
  MemberDTO isAdmin(int id);

  /**
   * Modify the member's information.
   *
   * @param memberDTO the member to modify
   * @return the modified member
   */
  MemberDTO modifyMember(MemberDTO memberDTO);

  /**
   * Change the state of the member to denied.
   *
   * @param refusalDTO the refusal information
   * @return true if deny complete, otherwise false
   */
  boolean denyMember(RefusalDTO refusalDTO);

  /**
   * Checks if the member exists into the database. if memberDTO is null it checks with idMember.
   *
   * @param memberDTO the member to check
   * @param idMember  the member's id to check
   * @return true if the member exists otherwise false
   */
  boolean memberExist(MemberDTO memberDTO, int idMember);

  /**
   * Get all interested members of the item identified by its id.
   *
   * @param idOffer the item's id
   * @return the list of interested members
   */
  List<MemberDTO> getInterestedMembers(int idOffer);
}
