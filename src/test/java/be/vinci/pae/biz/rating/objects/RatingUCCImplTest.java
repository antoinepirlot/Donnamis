package be.vinci.pae.biz.rating.objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.rating.interfaces.RatingUCC;
import be.vinci.pae.dal.rating.interfaces.RatingDAO;
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

class RatingUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

  private final RatingDAO ratingDAO = locator.getService(RatingDAO.class);

  private final DALServices dalServices = locator.getService(DALServices.class);

  private final RatingUCC ratingUCC = locator.getService(RatingUCC.class);

  private final RatingDTO ratingDTO = new RatingImpl();


  @BeforeEach
  void setUp() {
    try {
      Mockito.doNothing().when(this.dalServices).start();
      Mockito.doNothing().when(this.dalServices).commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void setEvaluateReturnedValue(boolean evaluated) {
    Mockito.when(this.ratingDAO.evaluate(this.ratingDTO)).thenReturn(evaluated);
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

  @DisplayName("Test evaluate added")
  @Test
  void testEvaluateAdded() {
    this.setEvaluateReturnedValue(true);
    assertTrue(this.ratingUCC.evaluate(this.ratingDTO));
  }


  @DisplayName("Test evaluate not added")
  @Test
  void testEvaluateNotAdded() {
    this.setEvaluateReturnedValue(false);
    assertFalse(this.ratingUCC.evaluate(this.ratingDTO));
  }

  @DisplayName("Test evaluate with start throwing SQL exception")
  @Test
  void testEvaluateWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.ratingUCC.evaluate(this.ratingDTO));
  }

  @DisplayName("Test evaluate with commit throwing SQL exception")
  @Test
  void testEvaluateWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.ratingUCC.evaluate(this.ratingDTO));
  }

  @Test
  void getAllRatingsOfMember() {

  }

  @Test
  void ratingExist() {
  }

}