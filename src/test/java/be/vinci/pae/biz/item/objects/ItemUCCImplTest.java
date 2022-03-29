package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.itemstype.interfaces.ItemsType;
import be.vinci.pae.biz.itemstype.objects.ItemsTypeImpl;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.utils.ApplicationBinder;
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
  private final ItemUCC itemUCC = locator.getService(ItemUCC.class);
  private final DALServices dalServices = locator.getService(DALServices.class);
  private final List<ItemDTO> itemDTOList = new ArrayList<>();
  private final ItemDTO goodItem = new ItemImpl();
  private final ItemDTO wrongItem = new ItemImpl();
  private final ItemDTO cancelledItem = new ItemImpl();
  private final int givenIdItem = 25;
  private final int assignedIdItem = 280;
  private final int notExistingIdItem = 56464;

  @BeforeEach
  void setUp() {
    this.itemDTOList.add(new ItemImpl());
    this.itemDTOList.add(new ItemImpl());
    this.goodItem.setId(5);
    this.goodItem.setItemDescription("Description");
    this.goodItem.setOfferStatus("donated");
    ItemsType itemType = new ItemsTypeImpl();
    itemType.setId(5);
    this.goodItem.setItemType(itemType);
    MemberDTO memberDTO = new MemberImpl();
    memberDTO.setId(89);
    this.cancelledItem.setOfferStatus("cancelled");
    this.goodItem.setMember(memberDTO);
    this.goodItem.setTitle("title");
    this.setMockitos();
  }

  private void setMockitos() {
    Mockito.when(this.itemDAO.getLatestItems()).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getAllItems()).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getAllOfferedItems()).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getOneItem(this.goodItem.getId())).thenReturn(this.goodItem);
    Mockito.when(this.itemDAO.getOneItem(this.notExistingIdItem)).thenReturn(null);
    Mockito.when(this.itemDAO.addItem(this.goodItem)).thenReturn(true);
    Mockito.when(this.itemDAO.addItem(this.wrongItem)).thenReturn(false);
    Mockito.when(this.itemDAO.addItem(null)).thenReturn(false);
    Mockito.when(this.itemDAO.cancelOffer(this.goodItem.getId())).thenReturn(this.cancelledItem);
    Mockito.when(this.itemDAO.cancelOffer(this.notExistingIdItem)).thenReturn(null);
    Mockito.when(this.itemDAO.cancelOffer(this.givenIdItem)).thenReturn(null);
    Mockito.when(this.itemDAO.cancelOffer(this.assignedIdItem)).thenReturn(this.cancelledItem);
  }

  private void setDALServices(boolean isWorking) {
    Mockito.when(this.dalServices.start()).thenReturn(null);
    Mockito.when(this.dalServices.rollback()).thenReturn(isWorking);
    Mockito.when(this.dalServices.commit()).thenReturn(isWorking);
  }

  @DisplayName("Test get latest items")
  @Test
  void testGetLatestItems() {
    this.setDALServices(true);
    assertEquals(this.itemDTOList, this.itemUCC.getLatestItems());
  }

  @DisplayName("Test get all items")
  @Test
  void testGetAllItems() {
    this.setDALServices(true);
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems());
  }

  @DisplayName("Test get all offered items")
  @Test
  void testGetAllOfferedItems() {
    this.setDALServices(true);
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems());
  }

  @DisplayName("Test get one item")
  @Test
  void testGetOneItem() {
    this.setDALServices(true);
    assertEquals(this.goodItem, this.itemUCC.getOneItem(this.goodItem.getId()));
  }

  @DisplayName("Test get one item with not existing id item")
  @Test
  void testGetOneItemWithNotExistingIdItem() {
    this.setDALServices(false);
    assertNull(this.itemUCC.getOneItem(this.notExistingIdItem));
  }

  @DisplayName("Test add item with good item")
  @Test
  void testAddItemWithGoodItem() {
    this.setDALServices(true);
    assertTrue(this.itemUCC.addItem(this.goodItem));
  }

  @DisplayName("Test add item with wrong item")
  @Test
  void testAddItemWithWrongItem() {
    this.setDALServices(false);
    assertFalse(this.itemUCC.addItem(this.wrongItem));
  }

  @DisplayName("Test add item with null item")
  @Test
  void testAddItemWithNullItem() {
    this.setDALServices(false);
    assertFalse(this.itemUCC.addItem(null));
  }

  @DisplayName("Test cancel offer with existing item")
  @Test
  void testCancelOfferWithExistingItem() {
    this.setDALServices(true);
    assertEquals(this.cancelledItem, this.itemUCC.cancelOffer(this.goodItem.getId()));
  }

  @DisplayName("Test cancel offer with not existing item")
  @Test
  void testCancelOfferWithNotExistingItem() {
    this.setDALServices(false);
    assertNull(this.itemUCC.cancelOffer(this.notExistingIdItem));
  }

  @DisplayName("Test cancel offer with given item")
  @Test
  void testCancelOfferWithGivenItem() {
    this.setDALServices(false);
    assertNull(this.itemUCC.cancelOffer(this.givenIdItem));
  }

  @DisplayName("Test cancel offer with assigned item")
  @Test
  void testCancelOfferWithAssignedItem() {
    this.setDALServices(true);
    assertEquals(this.cancelledItem, this.itemUCC.cancelOffer(this.assignedIdItem));
  }
}