package be.vinci.pae.biz.rating.objects;

import static org.junit.jupiter.api.Assertions.*;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.interest.objects.InterestImpl;
import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.rating.interfaces.RatingUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.rating.interfaces.RatingDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.SQLException;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
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

  @Test
  void evaluate() {
  }

  @Test
  void getAllRatingsOfMember() {

  }

  @Test
  void ratingExist() {
  }

}