package be.vinci.pae.dal.member.interfaces;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import java.sql.SQLException;
import java.util.List;

public interface MemberDAO {

  List<MemberDTO> getAllMembers();

  MemberDTO getOne(MemberDTO memberDTO) throws SQLException;

  MemberDTO getOne(int id) throws SQLException;

  /**
   * Add a new member to the db if it's not already in the db.
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been  registered
   */
  boolean register(MemberDTO memberDTO) throws SQLException;

  AddressDTO getAddressMember(int id);

  boolean confirmMember(MemberDTO memberDTO) throws SQLException;

  MemberDTO isAdmin(int id);

  MemberDTO modifyMember(MemberDTO memberDTO) throws SQLException;

  /**
   * Change the state of the member to denied.
   *
   * @param refusalDTO the refusal information
   * @return true if deny complete, otherwise false
   */
  boolean denyMember(RefusalDTO refusalDTO) throws SQLException;

  boolean memberExist(MemberDTO memberDTO, int idMember);

  /**
   * Get all interested members of the item identified by its id
   *
   * @param idOffer the item's id
   * @return the list of interested members
   */
  List<MemberDTO> getInterestedMembers(int idOffer) throws SQLException;
}
