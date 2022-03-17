package be.vinci.pae.biz;

import java.util.Objects;

public class ItemImpl implements Item {

  private int id;
  private String itemDescription;
  private int idItemType;
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

  public int getIdItemType() {
    return idItemType;
  }

  public void setIdItemType(int idItemType) {
    this.idItemType = idItemType;
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

  public void setOfferStatus(String offers_status) {
    this.offerStatus = offers_status;
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
        + ", id_item_type='" + idItemType + '\''
        + ", id_member='" + member + '\''
        + ", photo='" + photo + '\''
        + ", title=" + title
        + ", offer_status='" + offerStatus + '\''
        + '}';
  }


}
