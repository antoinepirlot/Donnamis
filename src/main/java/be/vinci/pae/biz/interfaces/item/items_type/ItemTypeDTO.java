package be.vinci.pae.biz.interfaces.item.items_type;

import be.vinci.pae.biz.objects.item.items_type.ItemTypeImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ItemTypeImpl.class)
public interface ItemTypeDTO {
  int getIdType();

  void setIdType(int idType);

  String getItemType();

  void setItemType(String itemType);
}
