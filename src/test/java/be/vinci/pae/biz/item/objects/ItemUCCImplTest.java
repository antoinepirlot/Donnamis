package be.vinci.pae.biz.item.objects;

import static org.junit.jupiter.api.Assertions.*;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
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
  private final List<ItemDTO> itemDTOList = new ArrayList<>();

  @BeforeEach
  void setUp() {
    this.itemDTOList.add(new ItemImpl());
    this.itemDTOList.add(new ItemImpl());
    this.setMockitos();
  }

  private void setMockitos() {
    Mockito.when(this.itemDAO.getLatestItems()).thenReturn(this.itemDTOList);
    Mockito.when(this.itemDAO.getAllItems()).thenReturn(this.itemDTOList);
  }

  @DisplayName("Test get latest items")
  @Test
  void testGetLatestItems() {
    assertEquals(this.itemDTOList, this.itemDAO.getLatestItems());
  }

  @DisplayName("Test get all items")
  @Test
  void testGetAllItems() {
    assertEquals(this.itemDTOList, this.itemDAO.getAllItems());
  }
}