package be.vinci.pae.biz;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = MemberImpl.class)
public interface MemberDTO {

  int getId();

  void setId(int id);

  String getUsername();

  void setUsername(String username);

  String getPassword();

  void setPassword(String password);

  String getLastName();

  void setLastName(String lastName);

  String getFirstName();

  void setFirstName(String firstName);

  boolean isAdmin();

  void setAdmin(boolean admin);

  String getActualState();

  void setActualState(String actualState);

  String getPhoneNumber();

  void setPhoneNumber(String phoneNumber);
}
