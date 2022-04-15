package be.vinci.pae.biz.itemstype.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ItemsTypeUCC {

  List<ItemsTypeDTO> getAll() throws SQLException;
}
