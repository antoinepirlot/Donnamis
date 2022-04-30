package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
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

class ItemUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

  private final ItemDAO itemDAO = locator.getService(ItemDAO.class);

  private final DALServices dalServices = locator.getService(DALServices.class);

  private final ItemUCC itemUCC = locator.getService(ItemUCC.class);

  private final ItemDTO itemDTO = new ItemImpl();

  private final String goodOfferStatus = "donated";

  private final List<ItemDTO> itemDTOList = new ArrayList<>();

  @BeforeEach
  void setUp() {

  }

  private void setGetAllItemsWorksFine(String offerStatus) {
    Mockito.when(this.itemDAO.getAllItems(offerStatus)).thenReturn(itemDTOList);
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

  @DisplayName("Test get all items with null offer status")
  @Test
  void testGetAllItemsWithNullOfferStatus() {
    this.setGetAllItemsWorksFine(null);
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems(null));
  }

  @DisplayName("Test get all items with donated offer status")
  @Test
  void testGetAllItemsWithDonatedOfferStatus() {
    this.setGetAllItemsWorksFine("donated");
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("donated"));
  }

  @DisplayName("Test get all items with assigned offer status")
  @Test
  void testGetAllItemsWithAssignedOfferStatus() {
    this.setGetAllItemsWorksFine("assigned");
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("assigned"));
  }

  @DisplayName("Test get all items with cancelled offer status")
  @Test
  void testGetAllItemsWithCancelledOfferStatus() {
    this.setGetAllItemsWorksFine("cancelled");
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("cancelled"));
  }

  @DisplayName("Test get all items with given offer status")
  @Test
  void testGetAllItemsWithGivenOfferStatus() {
    this.setGetAllItemsWorksFine("given");
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("given"));
  }

  @DisplayName("Test get all items with start throwing sql exception")
  @Test
  void testGetAllItemsWithStartThrowingSQLException() {
    this.setErrrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getAllItems(null));
  }

  @DisplayName("Test get all items with commit throwing sql exception")
  @Test
  void testGetAllItemsWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.getAllItems(null));
  }

  @Test
  void getOneItem() {
  }

  @Test
  void addItem() {
  }

  @Test
  void cancelItem() {
  }

  @Test
  void modifyItem() {
  }

  @Test
  void getAllItemsOfAMember() {
  }

  @Test
  void getAssignedItems() {
  }

  @Test
  void markItemAsGiven() {
  }

  @Test
  void markItemAsNotGiven() {
  }

  @Test
  void countNumberOfItemsByOfferStatus() {
  }

  @Test
  void countNumberOfReceivedOrNotReceivedItems() {
  }

  @Test
  void getMemberItemsByOfferStatus() {
  }

  @Test
  void getMemberReceivedItems() {
  }

  @Test
  void addPhoto() {
  }
}