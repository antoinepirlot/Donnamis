package be.vinci.pae.biz;

public interface Member extends MemberDTO {

  String createToken();

  void verifyState();
}
