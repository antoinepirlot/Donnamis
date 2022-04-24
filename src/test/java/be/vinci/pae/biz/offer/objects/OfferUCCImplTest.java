package be.vinci.pae.biz.offer.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.objects.ItemImpl;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
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
  void setUp() throws SQLException {
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

  private void setMockitos() throws SQLException {
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

  @DisplayName("Test create offer with good offer and existing item")
  @Test
  void testCreateOfferWithGoodOfferAndExistingItem() throws SQLException {
    assertTrue(this.offerUCC.createOffer(this.goodOfferExistingItem));
  }

  @DisplayName("Test create offer with good offer and not existing item")
  @Test
  void testCreateOfferWithGoodOfferAndNotExistingItem() throws SQLException {
    assertFalse(this.offerUCC.createOffer(this.goodOfferNotExistingItem));
  }

  @DisplayName("Test create offer with wrong offer and existing item")
  @Test
  void testCreateOfferWithWrongOfferAndExistingItem() throws SQLException {
    assertFalse(this.offerUCC.createOffer(this.wrongOfferWithExistingItem));
  }

  @DisplayName("Test create offer with wrong offer and not existing item")
  @Test
  void testCreateOfferWithWrongOfferAndNotExistingItem() throws SQLException {
    assertFalse(this.offerUCC.createOffer(this.wrongOfferWithNotExistingItem));
  }

  @DisplayName("Test create offer with empty offer")
  @Test
  void testCreateOfferWithEmptyOffer() throws SQLException {
    assertFalse(this.offerUCC.createOffer(this.emptyOffer));
  }

  @DisplayName("Test create offer with null offer")
  @Test
  void testCreateOfferWithNullOffer() throws SQLException {
    assertFalse(this.offerUCC.createOffer(null));
  }

  @DisplayName("Test get latest offers")
  @Test
  void testGetLatestOffers() throws SQLException {
    assertEquals(this.offerDTOList, this.offerUCC.getAllOffers(null));
  }

  @DisplayName("Test get all offers")
  @Test
  void testGetAllOffers() throws SQLException {
    assertEquals(this.offerDTOList, this.offerUCC.getAllOffers(null));
  }

  @DisplayName("Test get one offer with existing id offer")
  @Test
  void testGetOneOfferWithExistingIdOffer() throws SQLException {
    assertEquals(
        this.goodOfferExistingItem,
        this.offerUCC.getOneOffer(this.goodOfferExistingItem.getId())
    );
  }

  @DisplayName("Test get one offer with not existing id offer")
  @Test
  void testGetOneOfferWithNotExistingIdOffer() throws SQLException {
    assertNull(this.offerUCC.getOneOffer(this.notExistingIdOffer));
  }
}