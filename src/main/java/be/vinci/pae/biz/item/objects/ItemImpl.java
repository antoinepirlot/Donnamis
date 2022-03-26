package be.vinci.pae.biz.item.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.itemstype.interfaces.ItemType;
import be.vinci.pae.biz.itemstype.interfaces.ItemTypeDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
public class ItemImpl implements Item {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private String itemDescription;
  @JsonView(Views.Public.class)
  private ItemType itemType;
  @JsonView(Views.Public.class)
  private Member member;
  @JsonView(Views.Public.class)
  private String photo;
  @JsonView(Views.Public.class)
  private String title;
  @JsonView(Views.Public.class)
  private String offerStatus;
  @JsonView(Views.Public.class)
  private List<Offer> offerList = new ArrayList<>();

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

  public List<OfferDTO> getOfferList() {
    return new ArrayList<>(this.offerList);
  }

  public void setOfferList(List<OfferDTO> offerList) {
    List<Offer> temp = new ArrayList<>();
    temp.forEach(offer -> offerList.add((Offer) offer));
    this.offerList = temp;
  }

  @Override
  public void addOffer(OfferDTO offer) {
    this.offerList.add((Offer) offer);
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
