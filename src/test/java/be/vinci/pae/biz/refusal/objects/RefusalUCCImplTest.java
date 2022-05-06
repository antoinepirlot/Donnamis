package be.vinci.pae.biz.refusal.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalUCC;
import be.vinci.pae.dal.refusal.interfaces.RefusalDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.SQLException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RefusalUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
  private final RefusalDAO refusalDAO = locator.getService(RefusalDAO.class);
  private final DALServices dalServices = locator.getService(DALServices.class);
  private final RefusalUCC refusalUCC = locator.getService(RefusalUCC.class);
  private final RefusalDTO refusalDTO = new RefusalImpl();
  private final String username = "username";

  @BeforeEach
  void setUp() {
    try {
      Mockito.doNothing().when(this.dalServices).start();
      Mockito.doNothing().when(this.dalServices).commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    Mockito.when(this.refusalDAO.getRefusal(username)).thenReturn(this.refusalDTO);
  }

  private void setErrorDALServiceStart() {
    try {
      Mockito.doThrow(new SQLException()).when(dalServices).start();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void setErrorDALServiceCommit() {
    try {
      Mockito.doThrow(new SQLException()).when(dalServices).commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @DisplayName("Test get refusal working as expected")
  @Test
  void testGetRefusalWorkingAsExpected() {
    assertEquals(this.refusalDTO, this.refusalUCC.getRefusal(username));
  }

  @DisplayName("Test get refusal with start throwing sql exception")
  @Test
  void testGetRefusalWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.refusalUCC.getRefusal(username));
  }

  @DisplayName("Test get refusal with commit throwing sql exception")
  @Test
  void testGetRefusalWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.refusalUCC.getRefusal(username));
  }
}