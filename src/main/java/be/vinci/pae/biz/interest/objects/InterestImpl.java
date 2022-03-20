package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.Interest;
import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import java.util.Date;
import java.util.Objects;

public class InterestImpl implements Interest, InterestDTO {

  private int id;
  private boolean callWanted;
  private int idOffer;
  private int idMember;
  private Date date;

  public InterestImpl() {

  }

  @Override
  public int getIdInterest() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean isCallWanted() {
    return callWanted;
  }

  @Override
  public void setCallWanted(boolean callWanted) {
    this.callWanted = callWanted;
  }

  @Override
  public int getIdOffer() {
    return idOffer;
  }

  @Override
  public void setIdOffer(int idOffer) {
    this.idOffer = idOffer;
  }

  @Override
  public int getIdMember() {
    return idMember;
  }

  @Override
  public void setIdMember(int idMember) {
    this.idMember = idMember;
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
    return id == interest.id && callWanted == interest.callWanted
        && idOffer == interest.idOffer && idMember == interest.idMember
        && Objects.equals(date, interest.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, callWanted, idOffer, idMember, date);
  }

  @Override
  public String toString() {
    return "Interest{"
        + "id_interest=" + id
        + ", call_wanted=" + callWanted
        + ", id_offer=" + idOffer
        + ", id_member=" + idMember
        + ", date=" + date
        + '}';
  }
}
