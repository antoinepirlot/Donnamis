package be.vinci.pae.biz;

import static org.junit.jupiter.api.Assertions.*;

import be.vinci.pae.dal.MemberDAO;
import be.vinci.pae.utils.ApplicationBinder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MemberUCCImplTest {
  private MemberDAO memberDAO;
  private MemberUCC memberUCC;
  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

  @BeforeEach
  void setUp() {
    memberUCC = new MemberUCCImpl();
    memberDAO = locator.getService(MemberDAO.class);
  }

  private void configureMemberDTO(String actualState) {
    MemberDTO memberDTO = new MemberImpl();
    memberDTO.setActualState(actualState);
    Mockito.when(memberDAO.getOne("nico", "password"))
        .thenReturn(memberDTO);
  }

  @DisplayName("Test getAll")
  @Test
  void getAll() {
    //assertEquals();
  }

  @Test
  void loginConfirmedMember() {
    configureMemberDTO("confirmed");
    assertDoesNotThrow(
        () -> memberUCC.login("nico", "password"),
        "hje"
    );
  }
}