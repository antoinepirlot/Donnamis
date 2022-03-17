package be.vinci.pae.biz.objects.interest;

import be.vinci.pae.biz.interfaces.interest.Interest;
import be.vinci.pae.biz.interfaces.interest.InterestDTO;
import java.util.Date;
import java.util.Objects;

public class InterestImpl implements Interest, InterestDTO {

  private int id_interest;
  private boolean call_wanted;
  private int id_offer;
  private int id_member;
  private Date date;

  public InterestImpl() {

  }

  @Override
  public int getId_interest() {
    return id_interest;
  }

  @Override
  public void setId_interest(int id_interest) {
    this.id_interest = id_interest;
  }

  @Override
  public boolean isCall_wanted() {
    return call_wanted;
  }

  @Override
  public void setCall_wanted(boolean call_wanted) {
    this.call_wanted = call_wanted;
  }

  @Override
  public int getId_offer() {
    return id_offer;
  }

  @Override
  public void setId_offer(int id_offer) {
    this.id_offer = id_offer;
  }

  @Override
  public int getId_member() {
    return id_member;
  }

  @Override
  public void setId_member(int id_member) {
    this.id_member = id_member;
  }

  @Override
  public Date getDate() {
    return date;
  }

  @Override
  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InterestImpl interest = (InterestImpl) o;
    return id_interest == interest.id_interest && call_wanted == interest.call_wanted
        && id_offer == interest.id_offer && id_member == interest.id_member
        && Objects.equals(date, interest.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id_interest, call_wanted, id_offer, id_member, date);
  }

  @Override
  public String toString() {
    return "Interest{" +
        "id_interest=" + id_interest +
        ", call_wanted=" + call_wanted +
        ", id_offer=" + id_offer +
        ", id_member=" + id_member +
        ", date=" + date +
        '}';
  }
}
