package be.vinci.pae.biz.item.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.items_type.interfaces.ItemType;
import be.vinci.pae.biz.items_type.interfaces.ItemTypeDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import java.util.Objects;

public class ItemImpl implements Item {

  private int id;
  private String itemDescription;
  private ItemType itemType;
  private Member member;
  private String photo;
  private String title;
  private String offerStatus;

  public ItemImpl() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getItemDescription() {
    return itemDescription;
  }

  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  public ItemTypeDTO getItemType() {
    return itemType;
  }

  public void setItemType(ItemTypeDTO itemType) {
    this.itemType = (ItemType) itemType;
  }

  public MemberDTO getMember() {
    return member;
  }

  public void setMember(MemberDTO member) {
    this.member = (Member) member;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOfferStatus() {
    return offerStatus;
  }

  public void setOfferStatus(String offersStatus) {
    this.offerStatus = offersStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemImpl item = (ItemImpl) o;
    return id == item.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Item{"
        + "id=" + id
        + ", item_description='" + itemDescription + '\''
        + ", id_item_type='" + itemType + '\''
        + ", id_member='" + member + '\''
        + ", photo='" + photo + '\''
        + ", title=" + title
        + ", offer_status='" + offerStatus + '\''
        + '}';
  }


}
