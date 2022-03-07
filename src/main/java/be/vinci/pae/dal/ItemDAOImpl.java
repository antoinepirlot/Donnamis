package be.vinci.pae.dal;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.ItemDTO;
import jakarta.inject.Inject;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

  @Inject
  private final DALServices dalServices = new DALServices();
  private Factory factory;

  /**
   * Get the latest items from the database.
   *
   * @return List of the latest items offered
   */
  @Override
  public List<ItemDTO> getLatestItems() {
    return null;
  }
}
