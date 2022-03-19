package be.vinci.pae.biz.items_type.interfaces;

import be.vinci.pae.biz.items_type.objects.ItemTypeImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ItemTypeImpl.class)
public interface ItemTypeDTO {
  int getIdType();

  void setIdType(int idType);

  String getItemType();

  void setItemType(String itemType);
}
