package be.vinci.pae.biz.itemstype.objects;

import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeUCC;
import be.vinci.pae.dal.itemstype.interfaces.ItemsTypeDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.ihm.logs.LoggerHandler;
import jakarta.inject.Inject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemsTypeUCCImpl implements ItemsTypeUCC {

  @Inject
  private ItemsTypeDAO itemsTypeDAO;
  @Inject
  private DALServices dalServices;
  private Logger logger = LoggerHandler.getLogger();

  @Override
  public List<ItemsTypeDTO> getAll() {
    this.dalServices.start();
    List<ItemsTypeDTO> itemsTypeDTOList = this.itemsTypeDAO.getAll();
    if (itemsTypeDTOList == null) {
      this.dalServices.rollback();
      String message = "ERROR while getting all itemsType";
      this.logger.log(Level.INFO, message);
    } else {
      this.dalServices.commit();
    }
    return itemsTypeDTOList;
  }
}
