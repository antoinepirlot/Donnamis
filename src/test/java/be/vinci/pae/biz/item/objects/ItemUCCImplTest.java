package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    try {
      Mockito.doNothing().when(this.dalServices).start();
      Mockito.doNothing().when(this.dalServices).commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
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

  private void setCancelItemReturnedValue(int idItem) {
    if (idItem >= 1) {
      Mockito.when(this.itemDAO.cancelItem(idItem)).thenReturn(this.itemDTO);
    } else {
      Mockito.when(this.itemDAO.cancelItem(idItem)).thenReturn(null);
    }
  }

  private void setModifyItemReturnedValue(boolean modifyReturnedValue) {
    Mockito.when(this.itemDAO.modifyItem(this.itemDTO)).thenReturn(modifyReturnedValue);
  }

  private void setGetOneItemOfAMemberReturnedValue(int idMember) {
    if (idMember >= 1) {
      Mockito.when(this.itemDAO.getAllItemsOfAMember(idMember)).thenReturn(this.itemDTOList);
    } else {
      Mockito.when(this.itemDAO.getAllItemsOfAMember(idMember)).thenReturn(null);
    }
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

  @DisplayName("Test add item with commit throwing sql exception")
  @Test
  void testAddItemWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.addItem(this.itemDTO));
  }

  @DisplayName("Test cancel item with good id")
  @Test
  void testCancelItemWithGoodId() {
    this.setCancelItemReturnedValue(5);
    assertEquals(this.itemDTO, this.itemUCC.cancelItem(5));
  }

  @DisplayName("Test cancel item with wrong id")
  @Test
  void testCancelItemWithWrongId() {
    this.setCancelItemReturnedValue(-1);
    assertNull(this.itemUCC.cancelItem(-1));
  }

  @DisplayName("Test cancel item with start throwing sql exception")
  @Test
  void testCancelItemWithStartThrowingSQLException() {
    this.setErrrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.cancelItem(5));
  }

  @DisplayName("Test cancel item with commit throwing sql exception")
  @Test
  void testCancelItemWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.cancelItem(5));
  }

  @DisplayName("Test modify item with good item")
  @Test
  void testModifyItemWithGoodItem() {
    this.setModifyItemReturnedValue(true);
    assertTrue(this.itemUCC.modifyItem(this.itemDTO));
  }

  @DisplayName("Test modify item with wrong item")
  @Test
  void testModifyItemWithWrongItem() {
    this.setModifyItemReturnedValue(false);
    assertFalse(this.itemUCC.modifyItem(this.itemDTO));
  }

  @DisplayName("Test modify item with start throwing sql exception")
  @Test
  void testModifyItemWithStartThrowingSQLException() {
    this.setErrrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.modifyItem(this.itemDTO));
  }

  @DisplayName("Test modify item with commit throwing sql exception")
  @Test
  void testModifyItemWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.modifyItem(this.itemDTO));
  }

  @DisplayName("Test get all items of a member with good id")
  @Test
  void testGetAllItemsOfAMemberWithGoodId() {
    this.setGetOneItemOfAMemberReturnedValue(5);
    assertEquals(this.itemDTOList, this.itemUCC.getAllItemsOfAMember(5));
  }

  @DisplayName("Test get all items of a member with wrong id")
  @Test
  void testGetAllItemsOfAMemberWithWrongId() {
    this.setGetOneItemOfAMemberReturnedValue(-1);
    assertNull(this.itemUCC.getAllItemsOfAMember(-1));
  }

  @DisplayName("Test get all items of a member with start throwing sql exception")
  @Test
  void testGetAllItemsOfAMemberWithStartThrowingSQLException() {
    this.setErrrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getAllItemsOfAMember(5));
  }

  @DisplayName("Test get all items of a member with commit throwing sql exception")
  @Test
  void testGetAllItemsOfAMemberWithCommitId() {
    this.setErrrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getAllItemsOfAMember(5));
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