package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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

  private void setGetAllItemsReturnedValue(String offerStatus) {
    if (offerStatus == null
        || offerStatus.equals("donated") || offerStatus.equals("assigned")
        || offerStatus.equals("cancelled") || offerStatus.equals("given")
    ) {
      Mockito.when(this.itemDAO.getAllItems(offerStatus)).thenReturn(itemDTOList);
    } else {
      Mockito.when(this.itemDAO.getAllItems(offerStatus)).thenReturn(null);
    }
  }

  private void setGetOneItemReturnedValue(int idItem) {
    if (idItem >= 1) {
      Mockito.when(this.itemDAO.getOneItem(idItem)).thenReturn(this.itemDTO);
    } else {
      Mockito.when(this.itemDAO.getOneItem(idItem)).thenReturn(null);
    }
  }

  private void setAddItemReturnedValue(int newIdItem) {
    Mockito.when(this.itemDAO.addItem(this.itemDTO)).thenReturn(newIdItem);
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
    this.setGetAllItemsReturnedValue(null);
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems(null));
  }

  @DisplayName("Test get all items with donated offer status")
  @Test
  void testGetAllItemsWithDonatedOfferStatus() {
    this.setGetAllItemsReturnedValue("donated");
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("donated"));
  }

  @DisplayName("Test get all items with assigned offer status")
  @Test
  void testGetAllItemsWithAssignedOfferStatus() {
    this.setGetAllItemsReturnedValue("assigned");
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("assigned"));
  }

  @DisplayName("Test get all items with cancelled offer status")
  @Test
  void testGetAllItemsWithCancelledOfferStatus() {
    this.setGetAllItemsReturnedValue("cancelled");
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("cancelled"));
  }

  @DisplayName("Test get all items with given offer status")
  @Test
  void testGetAllItemsWithGivenOfferStatus() {
    this.setGetAllItemsReturnedValue("given");
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("given"));
  }

  @DisplayName("Test get all items with not valid offer status")
  @Test
  void testGetAllItemsWithNotValidStatus() {
    this.setGetAllItemsReturnedValue("not valid status");
    assertNull(this.itemUCC.getAllItems("not valid status"));
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

  @DisplayName("Test get one item with existing id")
  @Test
  void testGetOneItemWithGoodId() {
    this.setGetOneItemReturnedValue(4);
    assertEquals(this.itemDTO, this.itemUCC.getOneItem(4));
  }

  @DisplayName("Test get one item with not existing id")
  @Test
  void testGetOneItemWithNotExistingId() {
    this.setGetOneItemReturnedValue(-1);
    assertNull(this.itemUCC.getOneItem(-1));
  }

  @DisplayName("Test get one item with start throwing sql exception")
  @Test
  void testGetOneItemWithStartThrowingSQLException() {
    this.setErrrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getOneItem(4));
  }

  @DisplayName("Test get one item with commit throwing sql exception")
  @Test
  void testGetOneItemWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.getOneItem(4));
  }

  @DisplayName("Test add item with good item DTO")
  @Test
  void testAddItemWithGoodItemDTO() {
    int newIdItem = 5;
    this.setAddItemReturnedValue(newIdItem);
    assertEquals(newIdItem, this.itemUCC.addItem(this.itemDTO));
  }

  @DisplayName("Test add item with start throwing sql exception")
  @Test
  void testAddItemWithStartThrowingSQLException() {
    this.setErrrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.addItem(this.itemDTO));
  }

  @DisplayName("Test add item with start throwing sql exception")
  @Test
  void testAddItemWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.addItem(this.itemDTO));
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