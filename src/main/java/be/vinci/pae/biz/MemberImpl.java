package be.vinci.pae.biz;

class MemberImpl extends MemberDTO implements Member {

  public MemberImpl() {
  }

  public MemberImpl(int id, String username, String password, String lastName,
      String firstName, boolean isAdmin, String actualState, String phoneNumber) {
    super(id, username, password, lastName, firstName, isAdmin, actualState, phoneNumber);
  }
}
