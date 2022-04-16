package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.itemstype.interfaces.ItemsType;
import be.vinci.pae.biz.itemstype.objects.ItemsTypeImpl;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
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
  void setUp() throws SQLException {
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

  private void setMockitos() throws SQLException {
    Mockito.when(this.itemDAO.getAllItems("donated")).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getAllItems(null)).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getOneItem(this.goodItem.getId())).thenReturn(this.goodItem);
    Mockito.when(this.itemDAO.getOneItem(this.notExistingIdItem)).thenReturn(null);
    Mockito.when(this.itemDAO.addItem(this.goodItem)).thenReturn(this.goodItem.getId());
    Mockito.when(this.itemDAO.addItem(this.wrongItem)).thenReturn(-1);
    Mockito.when(this.itemDAO.addItem(null)).thenReturn(-1);
    Mockito.when(this.itemDAO.cancelItem(this.goodItem.getId())).thenReturn(this.cancelledItem);
    Mockito.when(this.itemDAO.cancelItem(this.notExistingIdItem)).thenReturn(null);
    Mockito.when(this.itemDAO.cancelItem(this.givenIdItem)).thenReturn(null);
    Mockito.when(this.itemDAO.cancelItem(this.assignedIdItem)).thenReturn(this.cancelledItem);
  }

  @DisplayName("Test get all items")
  @Test
  void testGetAllItems() throws SQLException {
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems(null));
  }

  @DisplayName("Test get all donated items")
  @Test
  void testGetAllOfferedItems() throws SQLException {
    assertEquals(this.itemDTOList, this.itemUCC.getAllItems("donated"));
  }

  @DisplayName("Test get one item")
  @Test
  void testGetOneItem() throws SQLException {
    assertEquals(this.goodItem, this.itemUCC.getOneItem(this.goodItem.getId()));
  }

  @DisplayName("Test get one item with not existing id item")
  @Test
  void testGetOneItemWithNotExistingIdItem() throws SQLException {
    assertNull(this.itemUCC.getOneItem(this.notExistingIdItem));
  }

  @DisplayName("Test add item with good item")
  @Test
  void testAddItemWithGoodItem() throws SQLException {
    assertEquals(this.goodItem.getId(), this.itemUCC.addItem(this.goodItem));
  }

  @DisplayName("Test add item with wrong item")
  @Test
  void testAddItemWithWrongItem() throws SQLException {
    assertEquals(-1, this.itemUCC.addItem(this.wrongItem));
  }

  @DisplayName("Test add item with null item")
  @Test
  void testAddItemWithNullItem() throws SQLException {
    assertEquals(-1, this.itemUCC.addItem(null));
  }

  @DisplayName("Test cancel offer with existing item")
  @Test
  void testCancelOfferWithExistingItem() throws SQLException {
    assertEquals(this.cancelledItem, this.itemUCC.cancelItem(this.goodItem.getId()));
  }

  @DisplayName("Test cancel offer with not existing item")
  @Test
  void testCancelOfferWithNotExistingItem() throws SQLException {
    assertNull(this.itemUCC.cancelItem(this.notExistingIdItem));
  }

  @DisplayName("Test cancel offer with given item")
  @Test
  void testCancelOfferWithGivenItem() throws SQLException {
    assertNull(this.itemUCC.cancelItem(this.givenIdItem));
  }

  @DisplayName("Test cancel offer with assigned item")
  @Test
  void testCancelOfferWithAssignedItem() throws SQLException {
    assertEquals(this.cancelledItem, this.itemUCC.cancelItem(this.assignedIdItem));
  }
}