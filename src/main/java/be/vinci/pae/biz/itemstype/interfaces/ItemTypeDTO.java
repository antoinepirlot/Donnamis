package be.vinci.pae.biz.itemstype.interfaces;

import be.vinci.pae.biz.itemstype.objects.ItemTypeImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ItemTypeImpl.class)
public interface ItemTypeDTO {
  int getId();

  void setId(int id);

  String getItemType();

  void setItemType(String itemType);
}
