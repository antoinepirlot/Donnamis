package be.vinci.pae.dal.itemstype.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.dal.itemstype.interfaces.ItemsTypeDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import be.vinci.pae.ihm.logs.LoggerHandler;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ItemsTypeDAOImpl implements ItemsTypeDAO {

  @Inject
  private Factory factory;
  @Inject
  private DALBackendService dalBackendService;
  private final Logger logger = LoggerHandler.getLogger();

  @Override
  public List<ItemsTypeDTO> getAll() {
    List<ItemsTypeDTO> itemsTypesToReturn = new ArrayList<>();
    String query = "SELECT id_type, item_type "
        + "FROM project_pae.items_types;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ItemsTypeDTO itemsTypeDTO = ObjectsInstanceCreator
              .createItemsTypeInstance(this.factory, rs);
          itemsTypesToReturn.add(itemsTypeDTO);
        }
        return itemsTypesToReturn;
      }
    } catch (SQLException e) {
      this.logger.log(Level.SEVERE, e.getMessage());
    }
    return null;
  }

}
