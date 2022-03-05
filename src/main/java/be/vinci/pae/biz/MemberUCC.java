package be.vinci.pae.biz;

import java.util.List;

public interface MemberUCC {

  //  /**
  //   * Get all members from the database.
  //   *
  //   * @return all members
  //   */
  //  List<MemberDTO> getAll();

  /**
   * Get the member from the db, checks its state.
   *
   * @param username of the member
   * @param password of the member
   */
  void login(String username, String password);
}
