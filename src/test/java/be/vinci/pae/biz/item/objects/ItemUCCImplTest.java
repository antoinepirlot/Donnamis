package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.*;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
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
  private final ItemDTO itemDTO = new ItemImpl();
  private final int notExistingIdItem = 56464;

  @BeforeEach
  void setUp() {
    this.itemDTOList.add(new ItemImpl());
    this.itemDTOList.add(new ItemImpl());
    this.itemDTO.setId(5);
    this.setMockitos();
  }

  private void setMockitos() {
    Mockito.when(this.itemDAO.getLatestItems()).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getAllItems()).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getAllOfferedItems()).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getOneItem(this.itemDTO.getId())).thenReturn(this.itemDTO);
    Mockito.when(this.itemDAO.getOneItem(this.notExistingIdItem)).thenReturn(null);
    Mockito.when(this.itemDAO.addItem(this.itemDTO)).thenReturn(true);
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
    assertEquals(this.itemDTO, this.itemUCC.getOneItem(this.itemDTO.getId()));
  }

  @DisplayName("Test get one item with not existing id item")
  @Test
  void testGetOneItemWithNotExistingIdItem() {
    assertNull(this.itemUCC.getOneItem(this.notExistingIdItem));
  }
}