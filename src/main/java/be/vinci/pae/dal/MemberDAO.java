package be.vinci.pae.dal;

import be.vinci.pae.biz.MemberDTO;
import java.util.List;

public interface MemberDAO {

  //  List<MemberDTO> getAll();

  List<MemberDTO> getAllMembers();

  MemberDTO getOne(String username);

  /**
   * Add a new member to the db if it's not already in the db.
   * @param username of the member we add into de DB
   * @param password of the member we add into de DB
   * @param firstName of the member we add into de DB
   * @param lastName of the member we add into de DB
   * @return true if the member has been registered
   */
  boolean register(String username, String password, String firstName, String lastName);
}
