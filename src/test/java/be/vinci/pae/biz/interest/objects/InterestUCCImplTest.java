package be.vinci.pae.biz.interest.objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
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

  private final OfferDAO offerDAO = locator.getService(OfferDAO.class);

  private final MemberDAO memberDAO = locator.getService(MemberDAO.class);


  private final DALServices dalServices = locator.getService(DALServices.class);

  private final InterestUCC interestUCC = locator.getService(InterestUCC.class);

  private final InterestDTO interestDTO = new InterestImpl();


  @BeforeEach
  void setUp() {
    Mockito.when(this.offerDAO.offerExist(this.interestDTO.getOffer())).thenReturn(true);
    Mockito.when(this.memberDAO.memberExist(this.interestDTO.getMember(), -1)).thenReturn(true);
    Mockito.when(this.interestDAO.interestExist(this.interestDTO)).thenReturn(false);
    try {
      Mockito.doNothing().when(this.dalServices).start();
      Mockito.doNothing().when(this.dalServices).commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void setInterestDAOMarkInterestReturnValue(boolean interestMarked) {
    Mockito.when(interestDAO.markInterest(interestDTO)).thenReturn(interestMarked);
  }

  private void setInterestDAOInterestExistsReturnValue(boolean interestMarked) {
    Mockito.when(interestDAO.interestExist(interestDTO)).thenReturn(interestMarked);
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

  /*
  @DisplayName("Test mark interest with interest marked")
  @Test
  void testMarkInterestWithInterestMarked() {
    this.setInterestDAOMarkInterestReturnValue(true);
    assertDoesNotThrow(() -> interestUCC.markInterest(interestDTO));
  }



  @DisplayName("Test mark interest with interest not marked")
  @Test
  void testMarkInterestWithInterestNotMarked() {
    this.setInterestDAOMarkInterestReturnValue(false);
    assertThrows(FatalException.class, () -> interestUCC.markInterest(interestDTO));
  }

   */

  @DisplayName("Test mark interest with start throwing sql exception ")
  @Test
  void testMarkInterestWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> interestUCC.markInterest(interestDTO));
  }

  @DisplayName("Test mark interest with commit throwing sql exception ")
  @Test
  void testMarkInterestWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> interestUCC.markInterest(interestDTO));
  }

  @DisplayName("Test interest exist with existing interest")
  @Test
  void testMarkInterestExistsWithExistingInterest() {
    this.setInterestDAOInterestExistsReturnValue(true);
    assertTrue(interestUCC.interestExist(interestDTO));
  }

  @DisplayName("Test interest exist with not existing interest")
  @Test
  void testMarkInterestExistsWithNotExistingInterest() {
    this.setInterestDAOInterestExistsReturnValue(false);
    assertFalse(interestUCC.interestExist(interestDTO));
  }

  @DisplayName("Test interest exists with start throwing sql exception")
  @Test
  void testInterestExistsWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.interestUCC.interestExist(interestDTO));
  }

  @DisplayName("Test interest exists with commit throwing sql exception")
  @Test
  void testInterestExistsWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.interestUCC.interestExist(interestDTO));
  }

}