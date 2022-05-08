package be.vinci.pae.biz.offer.objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.objects.ItemImpl;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OfferUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
  private final OfferDAO offerDAO = locator.getService(OfferDAO.class);
  private final OfferUCC offerUCC = locator.getService(OfferUCC.class);
  private final DALServices dalServices = locator.getService(DALServices.class);
  private final OfferDTO goodOfferExistingItem = new OfferImpl();
  private final OfferDTO goodOfferNotExistingItem = new OfferImpl();
  private final OfferDTO wrongOfferWithExistingItem = new OfferImpl();
  private final OfferDTO wrongOfferWithNotExistingItem = new OfferImpl();
  private final OfferDTO emptyOffer = new OfferImpl();
  private final ItemDTO existingItem = new ItemImpl();
  private final ItemDTO notExistingItem = new ItemImpl();
  private final List<OfferDTO> offerDTOList = new ArrayList<>();
  private final int notExistingIdOffer = 6464;

  @BeforeEach
  void setUp() {
    try {
      Mockito.doNothing().when(this.dalServices).start();
      Mockito.doNothing().when(this.dalServices).commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    this.goodOfferExistingItem.setDate(Timestamp.valueOf(LocalDateTime.now()));
    this.goodOfferExistingItem.setTimeSlot("Time slot");
    this.existingItem.setId(5);
    this.goodOfferExistingItem.setIdItem(this.existingItem.getId());
    this.goodOfferNotExistingItem.setIdItem(this.notExistingItem.getId());
    this.wrongOfferWithExistingItem.setIdItem(this.existingItem.getId());
    this.wrongOfferWithNotExistingItem.setIdItem(this.notExistingItem.getId());
    this.offerDTOList.add(new OfferImpl());
    this.offerDTOList.add(this.goodOfferExistingItem);
    this.setMockitos();
  }

  private void setMockitos() {
    Mockito.when(this.offerDAO.createOffer(this.goodOfferExistingItem)).thenReturn(true);
    Mockito.when(this.offerDAO.createOffer(this.goodOfferNotExistingItem)).thenReturn(false);
    Mockito.when(this.offerDAO.createOffer(this.wrongOfferWithExistingItem)).thenReturn(false);
    Mockito.when(this.offerDAO.createOffer(this.wrongOfferWithNotExistingItem)).thenReturn(false);
    Mockito.when(this.offerDAO.createOffer(this.emptyOffer)).thenReturn(false);
    Mockito.when(this.offerDAO.createOffer(null)).thenReturn(false);
    Mockito.when(this.offerDAO.getAllOffers(null)).thenReturn(this.offerDTOList);
    Mockito.when(this.offerDAO
            .getOne(this.goodOfferExistingItem.getId()))
        .thenReturn(this.goodOfferExistingItem);
    Mockito.when(this.offerDAO.getOne(this.notExistingIdOffer)).thenReturn(null);
  }

  private void setOfferExistsReturnedValue(boolean exists) {
    Mockito.when(this.offerDAO.offerExist(this.emptyOffer)).thenReturn(exists);
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

  @DisplayName("Test create offer with good offer and existing item")
  @Test
  void testCreateOfferWithGoodOfferAndExistingItem() {
    assertTrue(this.offerUCC.createOffer(this.goodOfferExistingItem));
  }

  @DisplayName("Test create offer with good offer and not existing item")
  @Test
  void testCreateOfferWithGoodOfferAndNotExistingItem() {
    assertThrows(FatalException.class,
        () -> this.offerUCC.createOffer(this.goodOfferNotExistingItem));
  }

  @DisplayName("Test create offer with wrong offer and existing item")
  @Test
  void testCreateOfferWithWrongOfferAndExistingItem() {
    assertThrows(FatalException.class,
        () -> this.offerUCC.createOffer(this.wrongOfferWithExistingItem));
  }

  @DisplayName("Test create offer with not existing item")
  @Test
  void testCreateOfferWithWrongOfferAndNotExistingItem() {
    assertThrows(FatalException.class, () -> this.offerUCC.createOffer(this.emptyOffer));
  }

  @DisplayName("Test create offer with start throwing sql exception")
  @Test
  void testCreateOfferWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.offerUCC.createOffer(this.emptyOffer));
  }

  @DisplayName("Test create offer with commit throwing sql exception")
  @Test
  void testCreateOfferWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.offerUCC.createOffer(this.emptyOffer));
  }

  @DisplayName("Test get latest offers")
  @Test
  void testGetLatestOffers() {
    assertEquals(this.offerDTOList, this.offerUCC.getAllOffers(null));
  }

  @DisplayName("Test get all offers")
  @Test
  void testGetAllOffers() {
    assertEquals(this.offerDTOList, this.offerUCC.getAllOffers(null));
  }

  @DisplayName("Test get all offers with start throwing sql exception")
  @Test
  void testGetAllOffersWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.offerUCC.getAllOffers(null));
  }

  @DisplayName("Test get all offers with commit throwing sql exception")
  @Test
  void testGetAllOffersWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.offerUCC.getAllOffers(null));
  }

  @DisplayName("Test get one offer with existing id offer")
  @Test
  void testGetOneOfferWithExistingIdOffer() {
    assertEquals(
        this.goodOfferExistingItem,
        this.offerUCC.getOneOffer(this.goodOfferExistingItem.getId())
    );
  }

  @DisplayName("Test get one offer with not existing id offer")
  @Test
  void testGetOneOfferWithNotExistingIdOffer() {
    assertThrows(ObjectNotFoundException.class,
        () -> this.offerUCC.getOneOffer(this.notExistingIdOffer));
  }

  @DisplayName("Test get one offer with start throwing sql exception")
  @Test
  void testGetOneOfferWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.offerUCC.getOneOffer(this.notExistingIdOffer));
  }

  @DisplayName("Test get one offer with commit throwing sql exception")
  @Test
  void testGetOneOfferWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.offerUCC.getOneOffer(this.notExistingIdOffer));
  }

  @DisplayName("Test get last two offers of with good item")
  @Test
  void testGetLastTwoOffersOfWithGoodItem() {
    assertDoesNotThrow(() -> this.offerDAO.getLastTwoOffersOf(this.existingItem));
  }

  @DisplayName("Test get last two offers of with null item")
  @Test
  void testGetLastTwoOffersOfWithNullItem() {
    assertDoesNotThrow(() -> this.offerDAO.getLastTwoOffersOf(null));
  }

  @DisplayName("Test get last two offers of with start throwing sql exception")
  @Test
  void testGetLastTwoOffersOfWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.offerUCC.getLastTwoOffersOf(this.existingItem));
  }

  @DisplayName("Test get last two offers of with commit throwing sql exception")
  @Test
  void testGetLastTwoOffersOfWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.offerUCC.getLastTwoOffersOf(this.existingItem));
  }

  @DisplayName("Test offer exists with existing offer")
  @Test
  void testOfferExistsWithExistingOffer() {
    this.setOfferExistsReturnedValue(true);
    assertTrue(this.offerUCC.offerExist(this.emptyOffer));
  }

  @DisplayName("Test offer exists with not existing offer")
  @Test
  void testOfferExistsWithNotExistingOffer() {
    this.setOfferExistsReturnedValue(false);
    assertFalse(this.offerUCC.offerExist(this.emptyOffer));
  }

  @DisplayName("Test offer exists with start throwing sql exception")
  @Test
  void testOfferExistsWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.offerUCC.offerExist(this.emptyOffer));
  }

  @DisplayName("Test offer exists with commit throwing sql exception")
  @Test
  void testOfferExistsWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.offerUCC.offerExist(this.emptyOffer));
  }
}