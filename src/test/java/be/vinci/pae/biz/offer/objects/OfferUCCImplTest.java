package be.vinci.pae.biz.offer.objects;

import static org.junit.jupiter.api.Assertions.*;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.objects.ItemImpl;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.Date;
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
  private final OfferDTO goodOfferExistingOffer = new OfferImpl();
  private final OfferDTO goodOfferNotExistingItem = new OfferImpl();
  private final OfferDTO wrongOfferWithExistingItem = new OfferImpl();
  private final OfferDTO wrongOfferWithNotExistingItem = new OfferImpl();
  private final OfferDTO emptyOffer = new OfferImpl();
  private final ItemDTO existingItem = new ItemImpl();
  private final ItemDTO notExistingItem = new ItemImpl();

  @BeforeEach
  void setUp() {
    this.goodOfferExistingOffer.setDate(new Date(25));
    this.goodOfferExistingOffer.setTimeSlot("Time slot");
    this.existingItem.setId(5);
    this.goodOfferExistingOffer.setItem(this.existingItem);
    this.goodOfferNotExistingItem.setItem(this.notExistingItem);
    this.wrongOfferWithExistingItem.setItem(this.existingItem);
    this.wrongOfferWithNotExistingItem.setItem(this.notExistingItem);
    this.setMockitos();
  }

  private void setMockitos() {
    Mockito.when(this.offerDAO.createOffer(this.goodOfferExistingOffer)).thenReturn(true);
    Mockito.when(this.offerDAO.createOffer(this.goodOfferNotExistingItem)).thenReturn(false);
    Mockito.when(this.offerDAO.createOffer(this.wrongOfferWithExistingItem)).thenReturn(false);
    Mockito.when(this.offerDAO.createOffer(this.wrongOfferWithNotExistingItem)).thenReturn(false);
    Mockito.when(this.offerDAO.createOffer(this.emptyOffer)).thenReturn(false);
    Mockito.when(this.offerDAO.createOffer(null)).thenReturn(false);
  }

  @DisplayName("Test create offer with good offer and existing item")
  @Test
  void testCreateOfferWithGoodOfferAndExistingItem() {
    assertTrue(this.offerUCC.createOffer(this.goodOfferExistingOffer));
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
}