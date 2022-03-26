package be.vinci.pae.biz.offer.objects;

import static org.junit.jupiter.api.Assertions.*;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.objects.ItemImpl;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.Date;
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
    this.goodOfferExistingItem.setDate(new Date(25));
    this.goodOfferExistingItem.setTimeSlot("Time slot");
    this.existingItem.setId(5);
    this.goodOfferExistingItem.setItem(this.existingItem);
    this.goodOfferNotExistingItem.setItem(this.notExistingItem);
    this.wrongOfferWithExistingItem.setItem(this.existingItem);
    this.wrongOfferWithNotExistingItem.setItem(this.notExistingItem);
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
    Mockito.when(this.offerDAO.getLatestOffers()).thenReturn(this.offerDTOList);
    Mockito.when(this.offerDAO.getAllOffers()).thenReturn(this.offerDTOList);
    Mockito.when(this.offerDAO
            .getOne(this.goodOfferExistingItem.getId()))
        .thenReturn(this.goodOfferExistingItem);
    Mockito.when(this.offerDAO.getOne(this.notExistingIdOffer)).thenReturn(null);
  }

  @DisplayName("Test create offer with good offer and existing item")
  @Test
  void testCreateOfferWithGoodOfferAndExistingItem() {
    assertTrue(this.offerUCC.createOffer(this.goodOfferExistingItem));
  }

  @DisplayName("Test create offer with good offer and not existing item")
  @Test
  void testCreateOfferWithGoodOfferAndNotExistingItem() {
    assertFalse(this.offerUCC.createOffer(this.goodOfferNotExistingItem));
  }

  @DisplayName("Test create offer with wrong offer and existing item")
  @Test
  void testCreateOfferWithWrongOfferAndExistingItem() {
    assertFalse(this.offerUCC.createOffer(this.wrongOfferWithExistingItem));
  }

  @DisplayName("Test create offer with wrong offer and not existing item")
  @Test
  void testCreateOfferWithWrongOfferAndNotExistingItem() {
    assertFalse(this.offerUCC.createOffer(this.wrongOfferWithNotExistingItem));
  }

  @DisplayName("Test create offer with empty offer")
  @Test
  void testCreateOfferWithEmptyOffer() {
    assertFalse(this.offerUCC.createOffer(this.emptyOffer));
  }

  @DisplayName("Test create offer with null offer")
  @Test
  void testCreateOfferWithNullOffer() {
    assertFalse(this.offerUCC.createOffer(null));
  }

  @DisplayName("Test get latest offers")
  @Test
  void testGetLatestOffers() {
    assertEquals(this.offerDTOList, this.offerUCC.getLatestOffers());
  }

  @DisplayName("Test get all offers")
  @Test
  void testGetAllOffers() {
    assertEquals(this.offerDTOList, this.offerUCC.getAllOffers());
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
    assertNull(this.offerUCC.getOneOffer(this.notExistingIdOffer));
  }
}