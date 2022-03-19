package be.vinci.pae.biz.member.objects;

import be.vinci.pae.biz.address.interfaces.Address;
import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import java.util.Objects;
import org.mindrot.jbcrypt.BCrypt;

public class MemberImpl implements Member {

  private int id;
  private String username;
  private String password;
  private String lastName;
  private String firstName;
  private boolean isAdmin;
  private String actualState;
  private String phoneNumber;
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

  public AddressDTO getAddress() {
    return address;
  }

  public void setAddress(AddressDTO addressDTO) {
    this.address = (Address) addressDTO;
  }

  /**
   * Verify if the state of the member is allowed to connect to the website. If the state is
   * "confirmed" the user can access.
   *
   * @return true if the actual state is "confirmed", else return false
   */
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
