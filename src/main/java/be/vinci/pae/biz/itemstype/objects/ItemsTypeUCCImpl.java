package be.vinci.pae.biz.itemstype.objects;

import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeUCC;
import be.vinci.pae.dal.itemstype.interfaces.ItemsTypeDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class ItemsTypeUCCImpl implements ItemsTypeUCC {

  @Inject
  private ItemsTypeDAO itemsTypeDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public List<ItemsTypeDTO> getAll() {
    try {
      this.dalServices.start();
      List<ItemsTypeDTO> itemsTypeDTOList = this.itemsTypeDAO.getAll();
      this.dalServices.commit();
      if (itemsTypeDTOList == null) {
        String message = "No items type found.";
        throw new ObjectNotFoundException(message);
      }
      return itemsTypeDTOList;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean exists(ItemsTypeDTO itemsTypeDTO) {
    try {
      this.dalServices.start();
      boolean exists = this.itemsTypeDAO.exists(itemsTypeDTO);
      this.dalServices.commit();
      return exists;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public void addItemsType(ItemsTypeDTO itemsTypeDTO) {
    if (this.exists(itemsTypeDTO)) {
      String message = "The items type " + itemsTypeDTO.getItemType() + " already exist";
      throw new ConflictException(message);
    }
    try {
      this.dalServices.start();
      boolean added = this.itemsTypeDAO.addItemsType(itemsTypeDTO);
      this.dalServices.commit();
      if (!added) {
        throw new FatalException("Unexpected exception while adding new itemsType");
      }
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }
}
