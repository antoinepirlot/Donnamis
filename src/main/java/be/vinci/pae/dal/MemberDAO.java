package be.vinci.pae.dal;

import be.vinci.pae.biz.MemberDTO;
import java.util.List;

public interface MemberDAO {

  //  List<MemberDTO> getAll();

  List<MemberDTO> getAllMembers();

  MemberDTO getOne(String username, String password);

  MemberDTO getOne(String username);
  //  MemberDTO createOne(MemberDTO memberDTO);

}
