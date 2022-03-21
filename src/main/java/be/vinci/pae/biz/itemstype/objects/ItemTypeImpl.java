package be.vinci.pae.biz.itemstype.objects;

import be.vinci.pae.biz.itemstype.interfaces.ItemType;

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
