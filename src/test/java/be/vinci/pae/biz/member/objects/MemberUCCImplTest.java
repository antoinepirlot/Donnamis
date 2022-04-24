package be.vinci.pae.biz.member.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.biz.refusal.objects.RefusalImpl;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.SQLException;
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
  private final MemberDTO memberDTO = new MemberImpl();
  private final MemberDTO memberToLogIn = new MemberImpl();
  private final RefusalDTO refusalDTO = new RefusalImpl();
  private String hashedPassword;
  private String password;
  private String wrongPassword;

  @BeforeEach
  void setUp() {
    hashedPassword = "$2a$10$vD5FXSmaNv4DkfpFfKfDsOjaJ192x2RdWyjIWr28lj5r1X9uvB9yC";
    password = "password";
    wrongPassword = "wrongpassword";
  }

  private void configureMemberDTO(String actualState, String password) throws SQLException {
    this.memberDTO.setId(99);
    this.memberDTO.setActualState(actualState);
    this.memberDTO.setPassword(this.hashedPassword);
    this.memberDTO.setUsername("nico");
    this.memberToLogIn.setUsername("nico");
    this.memberToLogIn.setPassword(password);
    this.refusalDTO.setIdRefusal(1);
    this.refusalDTO.setMember(this.memberDTO);
    this.refusalDTO.setText("Refus");
    Mockito.when(this.memberDAO.getOne(this.memberToLogIn))
        .thenReturn(this.memberDTO);
  }

  private void configureMemberDTOState(String state) throws SQLException {
    memberDTO.setActualState(state);
    memberDTO.setId(99);
    Mockito.when(memberDAO.confirmMember(this.memberDTO)).thenReturn(true);
    Mockito.when(memberDAO.denyMember(this.refusalDTO)).thenReturn(true);
    Mockito.when(memberDAO.getOne(99)).thenReturn(memberDTO);
    Mockito.when(memberDAO.isAdmin(99)).thenReturn(memberDTO);
  }

  //Test Confirm Member

  @DisplayName("Test Confirm Member with the state registered")
  @Test
  void testConfirmMemberWithStateRegistered() throws SQLException {
    configureMemberDTOState("registered");
    assertTrue(memberUCC.confirmMember(this.memberDTO));
  }

  @DisplayName("Test Confirm Member with the state denied")
  @Test
  void testConfirmMemberWithStateDenied() throws SQLException {
    configureMemberDTOState("denied");
    assertTrue(memberUCC.confirmMember(this.memberDTO));
  }

  @DisplayName("Test Confirm Member with the state confirmed")
  @Test
  void testConfirmMemberWithStateConfirmed() throws SQLException {
    configureMemberDTOState("confirmed");
    assertTrue(memberUCC.confirmMember(this.memberDTO));
  }

  //Test Deny Member

  @DisplayName("Test Deny Member With the state confirmedd")
  @Test
  void testDenyMemberWithStateConfirmed() throws SQLException {
    configureMemberDTOState("confirmed");
    assertTrue(memberUCC.denyMember(this.refusalDTO));
  }

  @DisplayName("Test Deny Member With the state registered")
  @Test
  void testDenyMemberWithStateRegistered() throws SQLException {
    configureMemberDTOState("registered");
    assertTrue(this.memberUCC.denyMember(this.refusalDTO));
  }

  @DisplayName("Test Deny Member With the state denied")
  @Test
  void testDenyMemberWithStateDenied() throws SQLException {
    configureMemberDTOState("denied");
    assertTrue(memberUCC.denyMember(this.refusalDTO));
  }

  //Test Confirm Admin

  @DisplayName("Test Confirm Admin With the state denied")
  @Test
  void testConfirmAdminWithStateRefused() throws SQLException {
    configureMemberDTOState("denied");
    assertTrue(this.memberUCC.confirmMember(this.memberDTO));
  }

  @DisplayName("Test Confirm Admin With the state registered")
  @Test
  void testConfirmAdminWithStateRegistered() throws SQLException {
    configureMemberDTOState("registered");
    assertTrue(memberUCC.confirmMember(this.memberDTO));
  }

  @DisplayName("Test Confirm Admin With the state confirmed")
  @Test
  void testConfirmAdminWithStateConfirmed() throws SQLException {
    configureMemberDTOState("confirmed");
    assertTrue(this.memberUCC.confirmMember(this.memberDTO));
  }

  //Test Login

  @DisplayName("Test login with confirmed member and good password")
  @Test
  void testLoginConfirmedMemberWithGoodPassword() throws SQLException {
    configureMemberDTO("confirmed", password);
    assertEquals(memberDTO,
        memberUCC.login(memberToLogIn)
    );
  }

  @DisplayName("Test login with denied member and good password")
  @Test
  void testLoginDeniedMemberWithGoodPassword() throws SQLException {
    configureMemberDTO("denied", password);
    assertNull(memberUCC.login(memberToLogIn));
  }

  @DisplayName("Test login with registered member and good password")
  @Test
  void testLoginRegisteredMemberWithGoodPassword() throws SQLException {
    configureMemberDTO("registered", password);
    assertNull(memberUCC.login(memberToLogIn));
  }

  @DisplayName("Test login with confirmed member and wrong password")
  @Test
  void testLoginConfirmedMemberWithWrongPassword() throws SQLException {
    configureMemberDTO("confirmed", wrongPassword);
    assertNull(memberUCC.login(memberToLogIn));
  }

  @DisplayName("Test login with registered member and wrong password")
  @Test
  void testLoginRegisteredMemberWithWrongPassword() throws SQLException {
    configureMemberDTO("registered", wrongPassword);
    assertNull(memberUCC.login(memberToLogIn));
  }

  @DisplayName("Test login with denied member and wrong password")
  @Test
  void testLoginDeniedMemberWithWrongPassword() throws SQLException {
    configureMemberDTO("denied", wrongPassword);
    assertNull(memberUCC.login(memberToLogIn));
  }
}