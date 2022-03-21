package be.vinci.pae.biz.offer.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.Offer;
import java.sql.Date;

public class OfferImpl implements Offer {

  private int idOffer;
  private Date date;
  private String timeSlot;
  private Item item;
  private Member member;

  public OfferImpl() {
    this.date = new Date(System.currentTimeMillis());
  }

  public int getIdOffer() {
    return idOffer;
  }

  public void setIdOffer(int idOffer) {
    this.idOffer = idOffer;
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
    return "OfferImpl{" +
        "idOffer=" + idOffer +
        ", date=" + date +
        ", time_slot='" + timeSlot + '\'' +
        ", item=" + item +
        '}';
  }
}
