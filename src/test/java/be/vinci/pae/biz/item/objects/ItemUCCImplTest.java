package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.*;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.itemstype.interfaces.ItemType;
import be.vinci.pae.biz.itemstype.objects.ItemTypeImpl;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
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
  private final List<ItemDTO> itemDTOList = new ArrayList<>();
  private final ItemDTO goodItem = new ItemImpl();
  private final ItemDTO wrongItem = new ItemImpl();
  private final int notExistingIdItem = 56464;

  @BeforeEach
  void setUp() {
    this.itemDTOList.add(new ItemImpl());
    this.itemDTOList.add(new ItemImpl());
    this.goodItem.setId(5);
    this.goodItem.setItemDescription("Description");
    ItemType itemType = new ItemTypeImpl();
    itemType.setId(5);
    this.goodItem.setItemType(itemType);
    MemberDTO memberDTO = new MemberImpl();
    memberDTO.setId(89);
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
  }

  @DisplayName("Test get latest items")
  @Test
  void testGetLatestItems() {
    assertEquals(this.itemDTOList, this.itemUCC.getLatestItems());
  }

  @DisplayName("Test get all items")
  @Test
  void testGetAllItems() {
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems());
  }

  @DisplayName("Test get all offered items")
  @Test
  void testGetAllOfferedItems() {
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems());
  }

  @DisplayName("Test get one item")
  @Test
  void testGetOneItem() {
    assertEquals(this.goodItem, this.itemUCC.getOneItem(this.goodItem.getId()));
  }

  @DisplayName("Test get one item with not existing id item")
  @Test
  void testGetOneItemWithNotExistingIdItem() {
    assertNull(this.itemUCC.getOneItem(this.notExistingIdItem));
  }

  @DisplayName("Test add item with good item")
  @Test
  void testAddItemWithGoodItem() {
    assertTrue(this.itemUCC.addItem(this.goodItem));
  }

  @DisplayName("Test add item with wrong item")
  @Test
  void testAddItemWithWrongItem() {
    assertFalse(this.itemUCC.addItem(this.wrongItem));
  }

  @DisplayName("Test add item with null item")
  @Test
  void testAddItemWithNullItem() {
    assertFalse(this.itemUCC.addItem(null));
  }
}