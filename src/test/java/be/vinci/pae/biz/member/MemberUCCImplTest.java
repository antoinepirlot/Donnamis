package be.vinci.pae.biz.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.utils.ApplicationBinder;
import dal.member.interfaces.MemberDAO;
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
  private MemberDTO memberDTO = new MemberImpl();

  @BeforeEach
  void setUp() {
    hashedPassword = "$2a$10$vD5FXSmaNv4DkfpFfKfDsOjaJ192x2RdWyjIWr28lj5r1X9uvB9yC";
    password = "password";
    wrongPassword = "wrongpassword";
  }

  private void configureMemberDTO(String actualState, String password) {
    memberDTO.setActualState(actualState);
    memberDTO.setPassword(hashedPassword);
    Mockito.when(memberDAO.getOne("nico"))
        .thenReturn(memberDTO);
  }

  private void configureMemberDTOState(String state) {
    memberDTO.setActualState(state);
    memberDTO.setId(99);
    Mockito.when(memberDAO.confirmMember(99)).thenReturn(memberDTO);
    Mockito.when(memberDAO.denyMember(99)).thenReturn(memberDTO);
    Mockito.when(memberDAO.getOneMember(99)).thenReturn(memberDTO);
    Mockito.when(memberDAO.isAdmin(99)).thenReturn(memberDTO);
  }

  //Test Confirm Member

  @DisplayName("Test Confirm Member with the state registered")
  @Test
  void testConfirmMemberWithStateRegistered() {
    configureMemberDTOState("registered");
    assertEquals(memberDTO, memberUCC.confirmMember(99));
  }

  @DisplayName("Test Confirm Member with the state denied")
  @Test
  void testConfirmMemberWithStateDenied() {
    configureMemberDTOState("denied");
    assertEquals(memberDTO, memberUCC.confirmMember(99));
  }

  @DisplayName("Test Confirm Member with the state confirmed")
  @Test
  void testConfirmMemberWithStateConfirmed() {
    configureMemberDTOState("confirmed");
    assertEquals(null, memberUCC.confirmMember(99));
  }

  //Test Deny Member

  @DisplayName("Test Deny Member With the state confirmedd")
  @Test
  void testDenyMemberWithStateConfirmed() {
    configureMemberDTOState("confirmed");
    assertEquals(null, memberUCC.denyMember(99));
  }

  @DisplayName("Test Deny Member With the state registered")
  @Test
  void testDenyMemberWithStateRegistered() {
    configureMemberDTOState("registered");
    assertEquals(memberDTO, memberUCC.denyMember(99));
  }

  @DisplayName("Test Deny Member With the state denied")
  @Test
  void testDenyMemberWithStateDenied() {
    configureMemberDTOState("denied");
    assertEquals(null, memberUCC.denyMember(99));
  }

  //Test Confirm Admin

  @DisplayName("Test Confirm Admin With the state denied")
  @Test
  void testConfirmAdminWithStateRefused() {
    configureMemberDTOState("denied");
    assertEquals(memberDTO, memberUCC.confirmAdmin(99));
  }

  @DisplayName("Test Confirm Admin With the state registered")
  @Test
  void testConfirmAdminWithStateRegistered() {
    configureMemberDTOState("registered");
    assertEquals(memberDTO, memberUCC.confirmAdmin(99));
  }

  @DisplayName("Test Confirm Admin With the state confirmed")
  @Test
  void testConfirmAdminWithStateConfirmed() {
    configureMemberDTOState("confirmed");
    assertEquals(null, memberUCC.confirmAdmin(99));
  }

  //Test Login

  @DisplayName("Test login with confirmed member and good password")
  @Test
  void testLoginConfirmedMemberWithGoodPassword() {
    configureMemberDTO("confirmed", password);
    assertEquals(memberDTO,
        memberUCC.login("nico", password)
    );
  }

  @DisplayName("Test login with denied member and good password")
  @Test
  void testLoginDeniedMemberWithGoodPassword() {
    configureMemberDTO("denied", password);
    assertNull(memberUCC.login("nico", password));
  }

  @DisplayName("Test login with registered member and good password")
  @Test
  void testLoginRegisteredMemberWithGoodPassword() {
    configureMemberDTO("registered", password);
    assertNull(memberUCC.login("nico", password));
  }

  @DisplayName("Test login with confirmed member and wrong password")
  @Test
  void testLoginConfirmedMemberWithWrongPassword() {
    configureMemberDTO("confirmed", wrongPassword);
    assertNull(memberUCC.login("nico", wrongPassword));
  }

  @DisplayName("Test login with registered member and wrong password")
  @Test
  void testLoginRegisteredMemberWithWrongPassword() {
    configureMemberDTO("registered", wrongPassword);
    assertNull(memberUCC.login("nico", wrongPassword));
  }

  @DisplayName("Test login with denied member and wrong password")
  @Test
  void testLoginDeniedMemberWithWrongPassword() {
    configureMemberDTO("denied", wrongPassword);
    assertNull(memberUCC.login("nico", wrongPassword));
  }
}