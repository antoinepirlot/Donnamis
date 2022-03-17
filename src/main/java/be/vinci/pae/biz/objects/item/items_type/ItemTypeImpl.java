package be.vinci.pae.biz.objects.item.items_type;

import be.vinci.pae.biz.interfaces.item.items_type.ItemType;

public class ItemTypeImpl implements ItemType {
  private int idType;

  private String itemType;

  public int getIdType() {
    return idType;
  }

  public void setIdType(int idType) {
    this.idType = idType;
  }

  public String getItemType() {
    return itemType;
  }

  public void setItemType(String itemType) {
    this.itemType = itemType;
  }
}
