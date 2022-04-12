package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.Interest;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import java.util.Objects;

@JsonInclude(Include.NON_NULL)
public class InterestImpl implements Interest {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private boolean callWanted;
  @JsonView(Views.Public.class)
  private int idItem;
  @JsonView(Views.Public.class)
  private Member member;
  @JsonView(Views.Public.class)
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
  public int getIdItem() {
    return idItem;
  }

  @Override
  public void setIdItem(int idItem) {
    this.idItem = idItem;
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
        && idItem == interest.idItem && member == interest.member
        && Objects.equals(date, interest.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, callWanted, idItem, member, date);
  }

  @Override
  public String toString() {
    return "Interest{"
        + "id_interest=" + id
        + ", call_wanted=" + callWanted
        + ", id_offer=" + idItem
        + ", id_member=" + member
        + ", date=" + date
        + '}';
  }
}
