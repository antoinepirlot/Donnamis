package be.vinci.pae.biz;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


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

  private final ServiceLocator LOCATOR = ServiceLocatorUtilities.bind(new ApplicationBinder());
  private final MemberDAO MEMBER_DAO = LOCATOR.getService(MemberDAO.class);
  private final MemberUCC MEMBER_UCC = LOCATOR.getService(MemberUCC.class);
  private String hashedPassword;
  private String password;
  private String wrongPassword;

  @BeforeEach
  void setUp() {
    hashedPassword = "$2a$10$vD5FXSmaNv4DkfpFfKfDsOjaJ192x2RdWyjIWr28lj5r1X9uvB9yC";
    password = "password";
    wrongPassword = "wrongpassword";
  }

  private void configureMemberDTO(String actualState, String password) {
    MemberDTO memberDTO = new MemberImpl();
    memberDTO.setActualState(actualState);
    memberDTO.setPassword(hashedPassword);
    Mockito.when(MEMBER_DAO.getOne("nico", password))
        .thenReturn(memberDTO);
  }

  @Test
  void testLoginConfirmedMemberWithGoodPassword() {
    configureMemberDTO("confirmed", password);
    assertDoesNotThrow(
        () -> MEMBER_UCC.login("nico", password)
    );
  }

  @DisplayName("Test login with denied member and good password")
  @Test
  void testLoginDeniedMemberWithGoodPassword() {
    configureMemberDTO("denied", password);
    assertThrows(
        WebApplicationException.class,
        () -> MEMBER_UCC.login("nico", password)
    );
  }

  @DisplayName("Test login with registered member and good password")
  @Test
  void testLoginRegisteredMemberWithGoodPassword() {
    configureMemberDTO("registered", password);
    assertThrows(
        WebApplicationException.class,
        () -> MEMBER_UCC.login("nico", password)
    );
  }

  @DisplayName("Test login with confirmed member and wrong password")
  @Test
  void testLoginConfirmedMemberWithWrongPassword() {
    configureMemberDTO("confirmed", wrongPassword);
    assertThrows(
        WebApplicationException.class,
        () -> MEMBER_UCC.login("nico", wrongPassword)
    );
  }

  @DisplayName("Test login with registered member and wrong password")
  @Test
  void testLoginRegisteredMemberWithWrongPassword() {
    configureMemberDTO("registered", wrongPassword);
    assertThrows(
        WebApplicationException.class,
        () -> MEMBER_UCC.login("nico", wrongPassword)
    );
  }

  @DisplayName("Test login with denied member and wrong password")
  @Test
  void testLoginDeniedMemberWithWrongPassword() {
    configureMemberDTO("denied", wrongPassword);
    assertThrows(
        WebApplicationException.class,
        () -> MEMBER_UCC.login("nico", wrongPassword)
    );
  }
}