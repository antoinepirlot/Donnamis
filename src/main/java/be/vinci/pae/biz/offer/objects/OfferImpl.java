package be.vinci.pae.biz.offer.objects;

import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.Offer;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@JsonInclude(Include.NON_NULL)
public class OfferImpl implements Offer {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private Timestamp date;
  @JsonView(Views.Public.class)
  private String timeSlot;
  @JsonView(Views.Public.class)
  private int idItem;
  @JsonView(Views.Public.class)
  private Member member;
  @JsonView(Views.Public.class)
  private int numberOfInterests;
  @JsonView(Views.Public.class)
  private int version;

  public OfferImpl() {
    this.date = Timestamp.valueOf(LocalDateTime.now());
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
  public Timestamp getDate() {
    return date;
  }

  @Override
  public void setDate(Timestamp date) {
    this.date = date;
  }

  @Override
  public String getTimeSlot() {
    return timeSlot;
  }

  @Override
  public void setTimeSlot(String timeSlot) {
    this.timeSlot = timeSlot;
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
  public Member getMember() {
    return member;
  }

  @Override
  public void setMember(MemberDTO memberDTO) {
    this.member = (Member) memberDTO;
  }

  @Override
  public int getNumberOfInterests() {
    return numberOfInterests;
  }

  @Override
  public void setNumberOfInterests(int numberOfInterests) {
    this.numberOfInterests = numberOfInterests;
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
  public String toString() {
    return "OfferImpl{"
        + "idOffer=" + id
        + ", date=" + date
        + ", time_slot='" + timeSlot + '\''
        + ", item=" + idItem
        + '}';
  }
}
