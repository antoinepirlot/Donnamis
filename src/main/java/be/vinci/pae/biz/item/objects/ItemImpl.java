package be.vinci.pae.biz.item.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.itemstype.interfaces.ItemsType;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
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
  private ItemsType itemType;
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
  @JsonView(Views.Public.class)
  private Offer lastOffer;
  @JsonView(Views.Public.class)
  private int version;

  public ItemImpl() {
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getItemDescription() {
    return itemDescription;
  }

  @Override
  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  @Override
  public ItemsTypeDTO getItemType() {
    return itemType;
  }

  @Override
  public void setItemType(ItemsTypeDTO itemType) {
    this.itemType = (ItemsType) itemType;
  }

  @Override
  public MemberDTO getMember() {
    return member;
  }

  @Override
  public void setMember(MemberDTO member) {
    this.member = (Member) member;
  }

  @Override
  public String getPhoto() {
    return photo;
  }

  @Override
  public void setPhoto(String photo) {
    this.photo = photo;
  }

  @Override
  public String getTitle() {
    return title;
  }

  @Override
  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getOfferStatus() {
    return offerStatus;
  }

  @Override
  public void setOfferStatus(String offersStatus) {
    this.offerStatus = offersStatus;
  }

  @Override
  public List<OfferDTO> getOfferList() {
    return new ArrayList<>(this.offerList);
  }

  @Override
  public void setOfferList(List<OfferDTO> offerList) {
    List<Offer> temp = new ArrayList<>();
    offerList.forEach(offer -> temp.add((Offer) offer));
    this.offerList = temp;
  }

  @Override
  public OfferDTO getLastOffer() {
    return lastOffer;
  }

  @Override
  public void setLastOffer(OfferDTO lastOffer) {
    this.lastOffer = (Offer) lastOffer;
  }

  @Override
  public void addOffer(Offer offer) {
    this.offerList.add(offer);
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
        + ", itemDescription='" + itemDescription + '\''
        + ", itemType='" + itemType + '\''
        + ", member='" + member + '\''
        + ", photo='" + photo + '\''
        + ", title=" + title
        + ", offerStatus='" + offerStatus + '\''
        + ", offerList" + offerList
        + ", lastOffer='" + lastOffer + '\''
        + '}';
  }
}
