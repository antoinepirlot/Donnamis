package be.vinci.pae.biz.itemstype.interfaces;

import be.vinci.pae.biz.itemstype.objects.ItemsTypeImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ItemsTypeImpl.class)
public interface ItemsTypeDTO {

  int getId();

  void setId(int id);

  String getItemType();

  void setItemType(String itemType);

  int getVersion();

  void setVersion(int version);
}
