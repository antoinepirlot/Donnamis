package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.objects.OfferImpl;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
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

  private final MemberDAO memberDAO = locator.getService(MemberDAO.class);

  private final OfferDAO offerDAO = locator.getService(OfferDAO.class);


  private final ItemDTO itemDTO = new ItemImpl();

  private final String goodOfferStatus = "donated";

  private final List<ItemDTO> itemDTOList = new ArrayList<>();

  @BeforeEach
  void setUp() {
    this.itemDTO.setMember(new MemberImpl());
    Mockito.when(this.itemDAO.itemExists(this.itemDTO.getId())).thenReturn(true);
    Mockito.when(this.itemDAO.getOneItem(this.itemDTO.getId())).thenReturn(this.itemDTO);

    this.itemDTO.setOfferStatus("");
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
      Mockito.when(this.itemDAO.itemExists(idItem)).thenReturn(true);
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
    Mockito.when(this.itemDAO.getOneItem(0)).thenReturn(itemDTO);
    Mockito.when(this.itemDAO.modifyItem(this.itemDTO)).thenReturn(modifyReturnedValue);
    Mockito.when(this.itemDAO.getOneItem(this.itemDTO.getId())).thenReturn(this.itemDTO);
    Mockito.when(this.itemDAO.itemExists(this.itemDTO.getId())).thenReturn(true);
  }

  private void setGetOneItemOfAMemberReturnedValue(int idMember) {
    if (idMember >= 1) {
      Mockito.when(this.itemDAO.getAllItemsOfAMember(idMember)).thenReturn(this.itemDTOList);
    } else {
      Mockito.when(this.itemDAO.getAllItemsOfAMember(idMember)).thenReturn(null);
    }
  }

  private void setGetAssignedItems(int idMember) {
    if (idMember >= 1) {
      Mockito.when(this.itemDAO.getAssignedItems(idMember)).thenReturn(this.itemDTOList);
      Mockito.when(this.memberDAO.memberExist(null, idMember)).thenReturn(true);
    } else {
      Mockito.when(this.itemDAO.getAssignedItems(idMember)).thenReturn(null);
      Mockito.when(this.memberDAO.memberExist(null, idMember)).thenReturn(false);
    }
  }

  private void setMarkItemAsrReturnedValue(boolean given) {
    Mockito.when(this.itemDAO.markItemAsGiven(this.itemDTO)).thenReturn(true);
    Mockito.when(this.itemDAO.markItemAsNotGiven(this.itemDTO)).thenReturn(true);
    Mockito.when(this.itemDAO.getOneItem(this.itemDTO.getId())).thenReturn(this.itemDTO);
  }

  private int setCountNumberOfItemsByOfferStatusReturnedValue(int idMember, String offerStatus) {
    int result = 7;
    if (idMember >= 1
        && (offerStatus.equals("donated")
        || offerStatus.equals("assigned")
        || offerStatus.equals("cancelled")
        || offerStatus.equals("given"))
    ) {
      Mockito.when(this.itemDAO.countNumberOfItemsByOfferStatus(idMember, offerStatus))
          .thenReturn(result);
    }
    return result;
  }

  private int setCountNumberOfReceivedOrNotReceivedItemsReturnedValue(int idMember,
      boolean received) {
    int result = 5;
    if (idMember >= 1) {
      Mockito.when(this.itemDAO.countNumberOfReceivedOrNotReceivedItems(idMember, received))
          .thenReturn(result);
      Mockito.when(this.memberDAO.memberExist(null, idMember)).thenReturn(true);
    } else {
      Mockito.when(this.memberDAO.memberExist(null, idMember)).thenReturn(false);
    }
    return result;
  }

  private void setGetMemberReceivedItemsReturnedValue(int idMember) {
    if (idMember >= 1) {
      Mockito.when(this.itemDAO.getMemberReceivedItems(idMember)).thenReturn(this.itemDTOList);
    } else {
      Mockito.when(this.itemDAO.getMemberReceivedItems(idMember)).thenReturn(null);
    }
  }

  private void setGetMemberItemsByOfferStatusReturnedValue(int idMember, String offerStatus) {
    if (idMember >= 1
        && (offerStatus.equals("donated")
        || offerStatus.equals("assigned")
        || offerStatus.equals("cancelled")
        || offerStatus.equals("given"))
    ) {
      Mockito.when(this.itemDAO.getMemberItemsByOfferStatus(idMember, offerStatus))
          .thenReturn(this.itemDTOList);
    } else {
      Mockito.when(this.itemDAO.getMemberItemsByOfferStatus(idMember, offerStatus))
          .thenReturn(null);
    }
  }

  private void setAddPhotoReturnedValue(int idItem, String photoName) {
    if (idItem >= 1 && photoName != null && !photoName.isBlank()) {
      Mockito.when(this.itemDAO.addPhoto(idItem, photoName)).thenReturn(true);
    } else {
      Mockito.when(this.itemDAO.addPhoto(idItem, photoName)).thenReturn(false);
    }
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

  @DisplayName("Test get all items with start throwing sql exception")
  @Test
  void testGetAllItemsWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
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
    assertEquals(this.itemDTO, this.itemUCC.getOneItem(null, 4));
  }

  @DisplayName("Test get one item with not existing id")
  @Test
  void testGetOneItemWithNotExistingId() {
    this.setGetOneItemReturnedValue(-1);
    assertThrows(ObjectNotFoundException.class, () -> this.itemUCC.getOneItem(null, -1));
  }

  @DisplayName("Test get one item with start throwing sql exception")
  @Test
  void testGetOneItemWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getOneItem(null, 4));
  }

  @DisplayName("Test get one item with commit throwing sql exception")
  @Test
  void testGetOneItemWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.getOneItem(null, 4));
  }

  @DisplayName("Test add item with good item DTO")
  @Test
  void testAddItemWithGoodItemDTO() {
    int newIdItem = 5;
    this.setAddItemReturnedValue(newIdItem);
    List<OfferDTO> list = new ArrayList<>();
    list.add(new OfferImpl());
    this.itemDTO.setOfferList(list);
    Mockito.when(this.offerDAO.createOffer(this.itemDTO.getOfferList().get(0))).thenReturn(true);
    assertEquals(newIdItem, this.itemUCC.addItem(this.itemDTO));
  }

  @DisplayName("Test add item with start throwing sql exception")
  @Test
  void testAddItemWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
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
    this.setErrorDALServiceStart();
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
    assertDoesNotThrow(() -> this.itemUCC.modifyItem(this.itemDTO));
  }

  @DisplayName("Test modify item with wrong item")
  @Test
  void testModifyItemWithWrongItem() {
    this.setModifyItemReturnedValue(false);
    assertThrows(FatalException.class, () -> this.itemUCC.modifyItem(this.itemDTO));
  }

  @DisplayName("Test modify item with start throwing sql exception")
  @Test
  void testModifyItemWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
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
    Mockito.when(this.memberDAO.memberExist(null, 5)).thenReturn(true);
    Mockito.when(this.itemDAO.itemExists(this.itemDTO.getId())).thenReturn(true);
    assertEquals(this.itemDTOList, this.itemUCC.getAllItemsOfAMember(5));
  }

  @DisplayName("Test get all items of a member with wrong id")
  @Test
  void testGetAllItemsOfAMemberWithWrongId() {
    this.setGetOneItemOfAMemberReturnedValue(-1);
    assertThrows(ObjectNotFoundException.class, () -> this.itemUCC.getAllItemsOfAMember(-1));
  }

  @DisplayName("Test get all items of a member with start throwing sql exception")
  @Test
  void testGetAllItemsOfAMemberWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getAllItemsOfAMember(5));
  }

  @DisplayName("Test get all items of a member with commit throwing sql exception")
  @Test
  void testGetAllItemsOfAMemberWithCommitId() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getAllItemsOfAMember(5));
  }

  @DisplayName("Test get assigned items of a member with good id")
  @Test
  void testGetAssignedItemsWithGoodId() {
    this.setGetAssignedItems(5);
    assertEquals(this.itemDTOList, this.itemUCC.getAssignedItems(5));
  }

  @DisplayName("Test get assigned items of a member with wrong id")
  @Test
  void testGetAssignedItemsWithWrongId() {
    this.setGetAssignedItems(-1);
    assertThrows(ObjectNotFoundException.class, () -> this.itemUCC.getAssignedItems(-1));
  }

  @DisplayName("Test get assigned items of a member with start throwing sql exception")
  @Test
  void testGetAssignedItemsWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getAssignedItems(5));
  }

  @DisplayName("Test get assigned items of a member with commit throwing sql exception")
  @Test
  void testGetAssignedItemsWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.getAssignedItems(5));
  }

  @DisplayName("Test mark item as given with good item")
  @Test
  void testMarkItemAsGivenWithGoodItem() {
    this.setMarkItemAsrReturnedValue(true);
    assertDoesNotThrow(() -> this.itemUCC.markItemAsGiven(this.itemDTO));
  }

  @DisplayName("Test mark item as given with wrong item")
  @Test
  void testMarkItemAsGivenWithWrongItem() {
    this.setMarkItemAsrReturnedValue(false);
    Mockito.when(this.itemDAO.itemExists(this.itemDTO.getId())).thenReturn(false);
    assertThrows(ObjectNotFoundException.class, () -> this.itemUCC.markItemAsGiven(this.itemDTO));
  }

  @DisplayName("Test mark item as given with start throwing sql exception")
  @Test
  void testMarkItemAsGivenWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.markItemAsGiven(this.itemDTO));
  }

  @DisplayName("Test mark item as given with commit throwing sql exception")
  @Test
  void testMarkItemAsGivenWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    this.setMarkItemAsrReturnedValue(true);
    Mockito.when(this.itemDAO.itemExists(this.itemDTO.getId())).thenReturn(true);
    assertThrows(FatalException.class, () -> this.itemUCC.markItemAsGiven(this.itemDTO));
  }

  @DisplayName("Test mark item as not given with good item")
  @Test
  void testMarkItemAsNotGivenWithGoodItem() {
    this.setMarkItemAsrReturnedValue(false);
    Mockito.when(this.itemDAO.itemExists(this.itemDTO.getId())).thenReturn(true);
    Mockito.when(this.itemDAO.getOneItem(this.itemDTO.getId())).thenReturn(this.itemDTO);
    assertDoesNotThrow(() -> this.itemUCC.markItemAsNotGiven(this.itemDTO));
  }

  @DisplayName("Test mark item as not given with wrong item")
  @Test
  void testMarkItemAsNotGivenWithWrongItem() {
    this.setMarkItemAsrReturnedValue(false);
    Mockito.when(this.itemDAO.itemExists(this.itemDTO.getId())).thenReturn(false);
    assertThrows(ObjectNotFoundException.class,
        () -> this.itemUCC.markItemAsNotGiven(this.itemDTO));
  }

  @DisplayName("Test mark item as not given with start throwing sql exception")
  @Test
  void testMarkItemAsNotGivenWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.markItemAsNotGiven(this.itemDTO));
  }

  @DisplayName("Test mark item as not given with commit throwing sql exception")
  @Test
  void testMarkItemAsNotGivenWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.markItemAsNotGiven(this.itemDTO));
  }

  @DisplayName("Test count number of items by offer status with donated offer status")
  @Test
  void testCountNumberOfItemsByOfferStatusWithDonatedOfferStatus() {
    int result =
        this.setCountNumberOfItemsByOfferStatusReturnedValue(5, "donated");
    assertEquals(result,
        this.itemUCC.countNumberOfItemsByOfferStatus(5, "donated"));
  }

  @DisplayName("Test count number of items by offer status with wrong id member")
  @Test
  void testCountNumberOfItemsByOfferStatusWithWrongIdMember() {
    int result =
        this.setCountNumberOfItemsByOfferStatusReturnedValue(-1, "donated");
    assertThrows(ObjectNotFoundException.class,
        () -> this.itemUCC.countNumberOfItemsByOfferStatus(-1, "donated"));
  }

  @DisplayName("Test count number of items by offer status with assigned offer status")
  @Test
  void testCountNumberOfItemsByOfferStatusWithAssignedOfferStatus() {
    int result =
        this.setCountNumberOfItemsByOfferStatusReturnedValue(5, "assigned");
    assertEquals(result,
        this.itemUCC.countNumberOfItemsByOfferStatus(5, "assigned"));
  }

  @DisplayName("Test count number of items by offer status with cancelled offer status")
  @Test
  void testCountNumberOfItemsByOfferStatusWithCancelledOfferStatus() {
    int result =
        this.setCountNumberOfItemsByOfferStatusReturnedValue(5, "cancelled");
    assertEquals(result,
        this.itemUCC.countNumberOfItemsByOfferStatus(5, "cancelled"));
  }

  @DisplayName("Test count number of items by offer status with given offer status")
  @Test
  void testCountNumberOfItemsByOfferStatusWithGivenOfferStatus() {
    int result =
        this.setCountNumberOfItemsByOfferStatusReturnedValue(5, "given");
    assertEquals(result,
        this.itemUCC.countNumberOfItemsByOfferStatus(5, "given"));
  }

  @DisplayName("Test count number of items by offer status with start throwing sql exception")
  @Test
  void testCountNumberOfItemsByOfferStatusWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class,
        () -> this.itemUCC.countNumberOfItemsByOfferStatus(5, "donated"));
  }

  @DisplayName("Test count number of items by offer status with commit throwing sql exception")
  @Test
  void testCountNumberOfItemsByOfferStatusWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class,
        () -> this.itemUCC.countNumberOfItemsByOfferStatus(5, "donated"));
  }

  @DisplayName("Test count number of received or not received items with received items")
  @Test
  void testCountNumberOfReceivedOrNotReceivedItemsWithReceivedItems() {
    int result = this.setCountNumberOfReceivedOrNotReceivedItemsReturnedValue(5, true);
    assertEquals(result, this.itemUCC.countNumberOfReceivedOrNotReceivedItems(5, true));
  }

  @DisplayName("Test count number of received or not received items with not received items")
  @Test
  void testCountNumberOfReceivedOrNotReceivedItemsWithNotReceivedItems() {
    int result = this.setCountNumberOfReceivedOrNotReceivedItemsReturnedValue(5, false);
    assertDoesNotThrow(() -> this.itemUCC.countNumberOfReceivedOrNotReceivedItems(5, false));
  }

  @DisplayName("Test count number of received or not received items with wrong id member")
  @Test
  void testCountNumberOfReceivedOrNotReceivedItemsWithWrongIdMember() {
    this.setCountNumberOfReceivedOrNotReceivedItemsReturnedValue(-1, true);
    assertThrows(
        ObjectNotFoundException.class,
        () -> this.itemUCC.countNumberOfReceivedOrNotReceivedItems(-1, true)
    );
  }

  @DisplayName("Test count number of received or not received items "
      + "with start throwing sql exception")
  @Test
  void testCountNumberOfReceivedOrNotReceivedItemsWithStartThrowingSQLExcepttion() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class,
        () -> this.itemUCC.countNumberOfReceivedOrNotReceivedItems(5, true));
  }

  @DisplayName("Test count number of received or not received items with commit "
      + "throwing sql exception")
  @Test
  void testCountNumberOfReceivedOrNotReceivedItemsWithCommitThrowingSQLExcepttion() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class,
        () -> this.itemUCC.countNumberOfReceivedOrNotReceivedItems(5, true));
  }

  @DisplayName("Test get member items by offer status with donated offer status")
  @Test
  void getMemberItemsByOfferStatusWithDonatedOfferStatus() {
    this.setGetMemberItemsByOfferStatusReturnedValue(5, "donated");
    assertEquals(this.itemDTOList,
        this.itemUCC.getMemberItemsByOfferStatus(5, "donated"));
  }

  @DisplayName("Test get member items by offer status with assigned offer status")
  @Test
  void getMemberItemsByOfferStatusWithAssignedOfferStatus() {
    this.setGetMemberItemsByOfferStatusReturnedValue(5, "assigned");
    assertEquals(this.itemDTOList,
        this.itemUCC.getMemberItemsByOfferStatus(5, "assigned"));
  }

  @DisplayName("Test get member items by offer status with cancelled offer status")
  @Test
  void getMemberItemsByOfferStatusWithCancelledOfferStatus() {
    this.setGetMemberItemsByOfferStatusReturnedValue(5, "cancelled");
    assertEquals(this.itemDTOList,
        this.itemUCC.getMemberItemsByOfferStatus(5, "cancelled"));
  }

  @DisplayName("Test get member items by offer status with given offer status")
  @Test
  void getMemberItemsByOfferStatusWithGivenOfferStatus() {
    this.setGetMemberItemsByOfferStatusReturnedValue(5, "given");
    assertEquals(this.itemDTOList,
        this.itemUCC.getMemberItemsByOfferStatus(5, "given"));
  }

  @DisplayName("Test get member items by offer status with wrong id member")
  @Test
  void getMemberItemsByOfferStatusWithWrongIdMember() {
    this.setGetMemberItemsByOfferStatusReturnedValue(-1, "donated");
    assertThrows(ObjectNotFoundException.class,
        () -> this.itemUCC.getMemberItemsByOfferStatus(-1, "donated"));
  }

  @DisplayName("Test get member items by offer status with start throwing sql exception")
  @Test
  void getMemberItemsByOfferStatusWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class,
        () -> this.itemUCC.getMemberItemsByOfferStatus(5, "donated"));
  }

  @DisplayName("Test get member items by offer status with commit throwing sql exception")
  @Test
  void getMemberItemsByOfferStatusWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class,
        () -> this.itemUCC.getMemberItemsByOfferStatus(5, "donated"));
  }

  @DisplayName("Test get member's received items with good member's id")
  @Test
  void testGetMemberReceivedItemsWithGoodId() {
    this.setGetMemberReceivedItemsReturnedValue(5);
    assertEquals(this.itemDTOList, this.itemUCC.getMemberReceivedItems(5));
  }

  @DisplayName("Test get member's received items with wrong member's id")
  @Test
  void testGetMemberReceivedItemsWithWrongId() {
    this.setGetMemberReceivedItemsReturnedValue(-1);
    assertThrows(ObjectNotFoundException.class, () -> this.itemUCC.getMemberReceivedItems(-1));
  }

  @DisplayName("Test get member's received items with start throwing sql exception")
  @Test
  void testGetMemberReceivedItemsWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.getMemberReceivedItems(5));
  }

  @DisplayName("Test get member's received items with commit throwing sql exception")
  @Test
  void testGetMemberReceivedItemsWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.getMemberReceivedItems(5));
  }

  @DisplayName("Test add photo with all working good")
  @Test
  void testAddPhotoWithAllWorkingGood() {
    this.setAddPhotoReturnedValue(5, "photo.png");
    Mockito.when(this.itemDAO.itemExists(5)).thenReturn(true);
    assertDoesNotThrow(() -> this.itemUCC.addPhoto(5, "photo.png"));
  }

  @DisplayName("Test add photo with start throwing sql exception")
  @Test
  void testAddPhotoWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.itemUCC.addPhoto(5, "photo.png"));
  }

  @DisplayName("Test add photo with commit throwing sql exception")
  @Test
  void testAddPhotoWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.itemUCC.addPhoto(5, "photo.png"));
  }
}