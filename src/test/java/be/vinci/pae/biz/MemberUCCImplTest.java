package be.vinci.pae.biz;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.dal.MemberDAO;
import be.vinci.pae.utils.ApplicationBinder;
import jakarta.ws.rs.WebApplicationException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MemberUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
  private final MemberDAO memberDAO = locator.getService(MemberDAO.class);
  private final MemberUCC memberUCC = locator.getService(MemberUCC.class);
  private String hashedPassword;
  private String password;
  private String wrongPassword;
  private MemberDTO memberDTO;

  @BeforeEach
  void setUp() {
    hashedPassword = "$2a$10$vD5FXSmaNv4DkfpFfKfDsOjaJ192x2RdWyjIWr28lj5r1X9uvB9yC";
    password = "password";
    wrongPassword = "wrongpassword";
  }

  private void configureMemberDTOForLogin(String actualState, String password) {
    memberDTO = new MemberImpl();
    memberDTO.setActualState(actualState);
    memberDTO.setPassword(hashedPassword);
    Mockito.when(memberDAO.getOne(password))
        .thenReturn(memberDTO);
  }

  private void configureMemberDTOForRegister(String username, String password, String lastName,
      String firstName) {
    memberDTO = new MemberImpl();
    memberDTO.setUsername(username);
    memberDTO.setPassword(password);
    memberDTO.setLastName(lastName);
    memberDTO.setFirstName(firstName);
  }

  @DisplayName("Test login with confirmed member and good password")
  @Test
  void testLoginConfirmedMemberWithGoodPassword() {
    configureMemberDTOForLogin("confirmed", password);
    assertEquals(memberDTO,
        memberUCC.login("nico", password)
    );
  }

  @DisplayName("Test login with denied member and good password")
  @Test
  void testLoginDeniedMemberWithGoodPassword() {
    configureMemberDTOForLogin("denied", password);
    assertThrows(
        WebApplicationException.class,
        () -> memberUCC.login("nico", password)
    );
  }

  @DisplayName("Test login with registered member and good password")
  @Test
  void testLoginRegisteredMemberWithGoodPassword() {
    configureMemberDTOForLogin("registered", password);
    assertThrows(
        WebApplicationException.class,
        () -> memberUCC.login("nico", password)
    );
  }

  @DisplayName("Test login with confirmed member and wrong password")
  @Test
  void testLoginConfirmedMemberWithWrongPassword() {
    configureMemberDTOForLogin("confirmed", wrongPassword);
    assertThrows(
        WebApplicationException.class,
        () -> memberUCC.login("nico", wrongPassword)
    );
  }

  @DisplayName("Test login with registered member and wrong password")
  @Test
  void testLoginRegisteredMemberWithWrongPassword() {
    configureMemberDTOForLogin("registered", wrongPassword);
    assertThrows(
        WebApplicationException.class,
        () -> memberUCC.login("nico", wrongPassword)
    );
  }

  @DisplayName("Test login with denied member and wrong password")
  @Test
  void testLoginDeniedMemberWithWrongPassword() {
    configureMemberDTOForLogin("denied", wrongPassword);
    assertThrows(
        WebApplicationException.class,
        () -> memberUCC.login("nico", wrongPassword)
    );
  }
}