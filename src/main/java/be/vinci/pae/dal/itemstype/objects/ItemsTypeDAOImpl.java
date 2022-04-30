package be.vinci.pae.dal.itemstype.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.dal.itemstype.interfaces.ItemsTypeDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class ItemsTypeDAOImpl implements ItemsTypeDAO {

  @Inject
  private Factory factory;
  @Inject
  private DALBackendService dalBackendService;

  @Override
  public List<ItemsTypeDTO> getAll() {
    List<ItemsTypeDTO> itemsTypesToReturn = new ArrayList<>();
    String query = "SELECT id_type, item_type, version_items_type "
        + "FROM project_pae.items_types;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          ItemsTypeDTO itemsTypeDTO = ObjectsInstanceCreator
              .createItemsTypeInstance(this.factory, rs);
          itemsTypesToReturn.add(itemsTypeDTO);
        }
        return itemsTypesToReturn.isEmpty() ? null : itemsTypesToReturn;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean exists(ItemsTypeDTO itemsTypeDTO) {
    String query = "SELECT DISTINCT item_type, version_items_type "
        + "FROM project_pae.items_types "
        + "WHERE item_type = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(itemsTypeDTO.getItemType()));
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean addItemsType(ItemsTypeDTO itemsTypeDTO) {
    String query = "INSERT INTO project_pae.items_types (item_type, version_items_type) "
        + "VALUES (?, 1);";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(itemsTypeDTO.getItemType()));
      return ps.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
