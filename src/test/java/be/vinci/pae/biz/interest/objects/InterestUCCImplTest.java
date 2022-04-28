package be.vinci.pae.biz.interest.objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
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

class InterestUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

  private final InterestDAO interestDAO = locator.getService(InterestDAO.class);

  private final DALServices dalServices = locator.getService(DALServices.class);

  private final InterestUCC interestUCC = locator.getService(InterestUCC.class);

  private InterestDTO interestDTO = new InterestImpl();


  @BeforeEach
  void setUp() {

  }

  private void setInterestDAOMarkInterestReturnValue(boolean interestMarked) {
    Mockito.when(interestDAO.markInterest(interestDTO)).thenReturn(interestMarked);
  }

  private void setInterestDAOInterestExistsReturnValue(boolean interestMarked) {
    Mockito.when(interestDAO.interestExist(interestDTO)).thenReturn(interestMarked);
  }

  private void setErrrorDALServiceStart() {
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

  @DisplayName("Test mark interest with successfully connection")
  @Test
  void testMarkInterestWithSuccessfullyConnection() {
    this.setInterestDAOMarkInterestReturnValue(true);
    assertTrue(interestUCC.markInterest(interestDTO));
  }

  @DisplayName("Test mark interest with start throwing sql exception ")
  @Test
  void testMarkInterestWithStartThrowingSQLException() {
    this.setErrrorDALServiceStart();
    assertThrows(FatalException.class, () -> interestUCC.markInterest(interestDTO));
  }

  @DisplayName("Test mark interest with commit throwing sql exception ")
  @Test
  void testMarkInterestWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> interestUCC.markInterest(interestDTO));
  }

  @DisplayName("Test interest exist with successfully connection")
  @Test
  void testMarkInterestExistsWithSuccessfullyConnection() {
    this.setInterestDAOInterestExistsReturnValue(true);
    assertTrue(interestUCC.interestExist(interestDTO));
  }

}