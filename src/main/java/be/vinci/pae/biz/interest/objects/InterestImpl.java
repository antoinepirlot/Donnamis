package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.Interest;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import java.sql.Timestamp;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
public class InterestImpl implements Interest {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private boolean callWanted;
  @JsonView(Views.Public.class)
  private Offer offer;
  @JsonView(Views.Public.class)
  private Member member;
  @JsonView(Views.Public.class)
  private Timestamp date;
  @JsonView(Views.Public.class)
  private int version;

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
  public void setOffer(OfferDTO offerDTO) {
    this.offer = (Offer) offerDTO;
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
  public Timestamp getDate() {
    return date;
  }

  @Override
  public void setDate(Timestamp date) {
    this.date = date;
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
