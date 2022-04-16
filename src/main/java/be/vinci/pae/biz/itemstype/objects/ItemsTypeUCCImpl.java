package be.vinci.pae.biz.itemstype.objects;

import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeUCC;
import be.vinci.pae.dal.itemstype.interfaces.ItemsTypeDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.ihm.logs.LoggerHandler;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

public class ItemsTypeUCCImpl implements ItemsTypeUCC {

  @Inject
  private ItemsTypeDAO itemsTypeDAO;
  @Inject
  private DALServices dalServices;
  private final Logger logger = LoggerHandler.getLogger();

  @Override
  public List<ItemsTypeDTO> getAll() throws SQLException {
    try {
      this.dalServices.start();
      List<ItemsTypeDTO> itemsTypeDTOList = this.itemsTypeDAO.getAll();
      this.dalServices.commit();
      return itemsTypeDTOList;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw e;
    }
  }
}
