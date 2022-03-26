package be.vinci.pae.biz.offer.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import java.sql.Date;

@JsonInclude(Include.NON_NULL)
public class OfferImpl implements Offer {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private Date date;
  @JsonView(Views.Public.class)
  private String timeSlot;
  @JsonView(Views.Public.class)
  private Item item;
  @JsonView(Views.Public.class)
  private Member member;

  public OfferImpl() {
    this.date = new Date(System.currentTimeMillis());
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getTimeSlot() {
    return timeSlot;
  }

  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
  }

  public ItemDTO getItem() {
    return item;
  }

  public void setItem(ItemDTO itemDTO) {
    this.item = (Item) itemDTO;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(MemberDTO memberDTO) {
    this.member = (Member) memberDTO;
  }

  @Override
  public String toString() {
    return "OfferImpl{"
        + "idOffer=" + id
        + ", date=" + date
        + ", time_slot='" + timeSlot + '\''
        + ", item=" + item
        + '}';
  }
}
