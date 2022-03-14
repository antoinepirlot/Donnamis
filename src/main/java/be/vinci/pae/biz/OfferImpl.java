package be.vinci.pae.biz;

import java.sql.Date;

public class OfferImpl implements Offer {

  private int idOffer;
  private Date date;
  private String time_slot;
  private Item item;

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

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }
}
