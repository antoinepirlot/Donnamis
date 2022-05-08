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
  @JsonView(Views.Public.class)
  private int version;

  public MemberImpl() {
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public void setUsername(String username) {
    if (username == null) {
      return;
    }
    this.username = username.toLowerCase().trim();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public boolean isAdmin() {
    return isAdmin;
  }

  @Override
  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }

  @Override
  public String getActualState() {
    return actualState;
  }

  @Override
  public void setActualState(String actualState) {
    this.actualState = actualState;
  }

  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public AddressDTO getAddress() {
    return address;
  }

  @Override
  public void setAddress(AddressDTO addressDTO) {
    this.address = (Address) addressDTO;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
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
