package be.vinci.pae.biz;

import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Objects;

class MemberImpl implements Member {

  private int id;
  private String username;
  private String password;
  private String lastName;
  private String firstName;
  private boolean isAdmin;
  private String actualState;
  private String phoneNumber;

  public MemberImpl() {
  }

  public MemberImpl(int id, String username, String password, String lastName, String firstName,
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

  /**
   * Create a connection token for a member
   *
   * @return the member's token
   */
  public String createToken() {
    System.out.println("Generating token.");
    Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
    String token =  JWT.create().withIssuer("auth0")
        .withClaim("username", this.username).sign(jwtAlgorithm);
    System.out.println("Token generated");
    return token;
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
