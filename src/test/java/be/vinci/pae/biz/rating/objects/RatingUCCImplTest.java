package be.vinci.pae.biz.rating.objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.item.objects.ItemImpl;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.rating.interfaces.RatingUCC;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.rating.interfaces.RatingDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RatingUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

  private final RatingDAO ratingDAO = locator.getService(RatingDAO.class);

  private final MemberDAO memberDAO = locator.getService(MemberDAO.class);

  private final DALServices dalServices = locator.getService(DALServices.class);

  private final RatingUCC ratingUCC = locator.getService(RatingUCC.class);

  private final RatingDTO ratingDTO = new RatingImpl();

  private final List<RatingDTO> ratingDTOList = new ArrayList<>();


  @BeforeEach
  void setUp() {
    this.ratingDTO.setMember(new MemberImpl());
    this.ratingDTO.setItem(new ItemImpl());
    MemberDTO memberDTO = new MemberImpl();
    memberDTO.setId(4);
    this.ratingDTO.getItem().setMember(memberDTO);
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

  private void setRatingExistReturnedValue(boolean evaluated) {
    Mockito.when(this.ratingDAO.ratingExist(this.ratingDTO)).thenReturn(evaluated);
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

  private void setGetAllRatingsOfMembre(int idMember) {
    if (idMember >= 1) {
      Mockito.when(this.ratingDAO.getAllRatingsOfMember(idMember)).thenReturn(this.ratingDTOList);
      Mockito.when(this.memberDAO.memberExist(null, idMember)).thenReturn(true);
    } else {
      Mockito.when(this.ratingDAO.getAllRatingsOfMember(idMember)).thenReturn(null);
      Mockito.when(this.memberDAO.memberExist(null, idMember)).thenReturn(false);
    }
  }

  @DisplayName("Test evaluate added")
  @Test
  void testEvaluateAdded() {
    this.setEvaluateReturnedValue(true);
    assertDoesNotThrow(() -> this.ratingUCC.evaluate(this.ratingDTO));
  }


  @DisplayName("Test evaluate not added")
  @Test
  void testEvaluateNotAdded() {
    this.setEvaluateReturnedValue(false);
    assertThrows(FatalException.class, () -> this.ratingUCC.evaluate(this.ratingDTO));
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

  @DisplayName("Test get all rating as expected")
  @Test
  void testGetAllRatingsOfMemberAsExpected() {
    this.setGetAllRatingsOfMembre(5);
    assertEquals(this.ratingDTOList, this.ratingUCC.getAllRatingsOfMember(5));
  }

  @DisplayName("Test get all rating with start throwing SQLException")
  @Test
  void testGetAllRatingsOfMemberWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () ->  this.ratingUCC.getAllRatingsOfMember(5));
  }

  @DisplayName("Test get all rating with commit throwing SQLException")
  @Test
  void testGetAllRatingsOfMemberWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.ratingUCC.getAllRatingsOfMember(5));
  }


  @DisplayName("Test rating exist")
  @Test
  void testRatingExist() {
    this.setRatingExistReturnedValue(true);
    assertTrue(this.ratingUCC.ratingExist(this.ratingDTO));
  }


  @DisplayName("Test rating not exist")
  @Test
  void testRatingNotExist() {
    this.setRatingExistReturnedValue(false);
    assertFalse(this.ratingUCC.ratingExist(this.ratingDTO));
  }

  @DisplayName("Test evaluate with start throwing SQL exception")
  @Test
  void testRatingExistWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.ratingUCC.ratingExist(this.ratingDTO));
  }

  @DisplayName("Test evaluate with commit throwing SQL exception")
  @Test
  void testExistWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.ratingUCC.ratingExist(this.ratingDTO));
  }

}