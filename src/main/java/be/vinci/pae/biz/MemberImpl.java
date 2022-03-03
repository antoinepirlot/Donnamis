package be.vinci.pae.biz;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
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
   * Verify if the state of the member is allowed to connect to the website. If the state is
   * "confirmed" the user can access.
   *
   * @throws WebApplicationException if the state is "registered" or "denied"
   */
  @Override
  public void verifyState() {
    System.out.println("L'état est :" + this.actualState);
    if (this.actualState.equals("confirmed")) {
      return;
    }
    if (this.actualState.equals("registered")) {
      throw new WebApplicationException(Response.status(Status.FORBIDDEN)
          .entity("The member is registered")
          .type("text/plain")
          .build());
    }
    if (this.actualState.equals("denied")) {
      throw new WebApplicationException(Response.status(Status.FORBIDDEN)
          .entity("The member is denied")
          .type("text/plain")
          .build());
    }


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
