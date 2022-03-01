package be.vinci.pae.biz;

import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class MemberDTO {

  private int id;
  private String username;
  private String password;
  private String lastName;
  private String firstName;
  private boolean isAdmin;
  private String actualState;
  private String phoneNumber;

  public MemberDTO() {
  }

  public MemberDTO(int id, String username, String password, String lastName, String firstName,
      boolean isAdmin, String actualState, String phoneNumber) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.lastName = lastName;
    this.firstName = firstName;
    this.isAdmin = isAdmin;
    this.actualState = actualState;
    this.phoneNumber = phoneNumber;
  }


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }

  public String getActualState() {
    return actualState;
  }

  public void setActualState(String actualState) {
    this.actualState = actualState;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public boolean checkPassword(String passwordToCheck) {
    return BCrypt.checkpw(passwordToCheck, this.password);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MemberDTO memberDTO = (MemberDTO) o;
    return id == memberDTO.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Member{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", lastName='" + lastName + '\'' +
        ", firstName='" + firstName + '\'' +
        ", isAdmin=" + isAdmin +
        ", actualState='" + actualState + '\'' +
        ", phoneNumber='" + phoneNumber + '\'' +
        '}';
  }
}
