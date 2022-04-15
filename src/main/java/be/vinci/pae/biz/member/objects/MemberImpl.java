package be.vinci.pae.biz.member.objects;

import be.vinci.pae.biz.address.interfaces.Address;
import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class MemberImpl implements Member {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private String username;
  @JsonView(Views.Internal.class)
  private String password;
  @JsonView(Views.Public.class)
  private String lastName;
  @JsonView(Views.Public.class)
  private String firstName;
  @JsonView(Views.Public.class)
  private boolean isAdmin;
  @JsonView(Views.Public.class)
  private String actualState;
  @JsonView(Views.Public.class)
  private String phoneNumber;
  @JsonView(Views.Public.class)
  private Address address;

  public MemberImpl() {
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
    this.username = username.toLowerCase().trim();
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

  public AddressDTO getAddress() {
    return address;
  }

  public void setAddress(AddressDTO addressDTO) {
    this.address = (Address) addressDTO;
  }

  @Override
  public boolean verifyState(String expectedState) {
    return this.actualState.equals(expectedState);

  }

  @Override
  public boolean checkPassword(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }

  @Override
  public void hashPassword() {
    this.password = BCrypt.hashpw(this.password, BCrypt.gensalt(10));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Member member = (Member) o;
    return this.id == member.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  @Override
  public String toString() {
    return "Member{"
        + "id=" + id
        + ", username='" + username + '\''
        + ", password='" + password + '\''
        + ", lastName='" + lastName + '\''
        + ", firstName='" + firstName + '\''
        + ", isAdmin=" + isAdmin
        + ", actualState='" + actualState + '\''
        + ", phoneNumber='" + phoneNumber + '\''
        + '}';
  }
}
