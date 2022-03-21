package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.Interest;
import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import java.util.Date;
import java.util.Objects;

public class InterestImpl implements Interest, InterestDTO {

  private int id;
  private boolean callWanted;
  private Offer offer;
  private Member member;
  private Date date;

  public InterestImpl() {

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
  public boolean isCallWanted() {
    return callWanted;
  }

  @Override
  public void setCallWanted(boolean callWanted) {
    this.callWanted = callWanted;
  }

  @Override
  public OfferDTO getOffer() {
    return offer;
  }

  @Override
  public void setOffer(OfferDTO offer) {
    this.offer = (Offer) offer;
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
        && offer == interest.offer && member == interest.member
        && Objects.equals(date, interest.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, callWanted, offer, member, date);
  }

  @Override
  public String toString() {
    return "Interest{"
        + "id_interest=" + id
        + ", call_wanted=" + callWanted
        + ", id_offer=" + offer
        + ", id_member=" + member
        + ", date=" + date
        + '}';
  }
}
