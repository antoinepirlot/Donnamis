package be.vinci.pae.biz;

import static org.junit.jupiter.api.Assertions.*;

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
  @BeforeEach
  void setUp() {

  }

  private void configureMemberDTO(String actualState) {
    MemberDTO memberDTO = new MemberImpl();
    memberDTO.setActualState(actualState);
    Mockito.when(MEMBER_DAO.getOne("nico", "password"))
        .thenReturn(memberDTO);
  }

  @Test
  void testLoginConfirmedMember() {
    configureMemberDTO("confirmed");
    assertDoesNotThrow(
        () -> MEMBER_UCC.login("nico", "password")
    );
  }

  @DisplayName("Test login with denied member")
  @Test
  void testLoginDeniedMember() {
    configureMemberDTO("denied");
    assertThrows(
        WebApplicationException.class,
        () -> MEMBER_UCC.login("nico", "password")
    );
  }

  @DisplayName("Test login with registered member")
  @Test
  void testLoginRegisteredMember() {
    configureMemberDTO("registered");
    assertThrows(
        WebApplicationException.class,
        () -> MEMBER_UCC.login("nico", "password")
    );
  }
}