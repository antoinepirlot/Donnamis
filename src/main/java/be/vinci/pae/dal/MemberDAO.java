package be.vinci.pae.dal;

import be.vinci.pae.biz.MemberDTO;

public interface MemberDAO {

  //  List<MemberDTO> getAll();

  MemberDTO getOne(String username, String password);

  //  MemberDTO createOne(MemberDTO memberDTO);

  boolean checkPassword(String password, String hashedPassword);
}
