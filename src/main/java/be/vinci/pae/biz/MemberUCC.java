package be.vinci.pae.biz;

import java.util.List;

public interface MemberUCC {

  /**
   * Get all members from the database.
   *
   * @return all members
   */
  List<MemberDTO> getAll();

  /**
   * Get the member from the db, checks its state and return a token for the member if it's
   * allowed.
   *
   * @param username of the member
   * @param password of the member
   * @return a token for the member
   */
  String login(String username, String password);
}
