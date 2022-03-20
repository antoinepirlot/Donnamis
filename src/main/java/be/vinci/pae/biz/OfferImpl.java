package be.vinci.pae.biz;

import java.sql.Date;

public class OfferImpl implements Offer {

  private int idOffer;
  private Date date;
  private String time_slot;
  private Item item;
  private Member member;

  public OfferImpl() {

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

  public String getTime_slot() {
    return time_slot;
  }

  public void setTime_slot(String time_slot) {
    this.time_slot = time_slot;
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
        ", time_slot='" + time_slot + '\'' +
        ", item=" + item +
        '}';
  }
}
