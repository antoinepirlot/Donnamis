package be.vinci.pae.biz.itemstype.objects;

import be.vinci.pae.biz.itemstype.interfaces.ItemsType;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class ItemsTypeImpl implements ItemsType {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private String itemType;
  @JsonView(Views.Public.class)
  private int version;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getItemType() {
    return itemType;
  }

  @Override
  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return "ItemsTypeImpl{"
        + "id=" + id
        + ", itemType='" + itemType + '\''
        + ", version=" + version
        + '}';
  }
}
