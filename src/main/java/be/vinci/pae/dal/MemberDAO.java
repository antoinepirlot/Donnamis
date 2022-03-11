package be.vinci.pae.dal;

import be.vinci.pae.biz.MemberDTO;
import java.util.List;

public interface MemberDAO {

  //  List<MemberDTO> getAll();

  List<MemberDTO> getAllMembers();

  MemberDTO getOne(String username, String password);

  /**
   * Add a new member to the db if it's not already in the db.
   * @param memberDTO the memberDTO to add in the db
   * @return the new created member if it's not already into the db otherwise null
   */
  boolean register(MemberDTO memberDTO);
}
