package be.vinci.pae.biz.refusal.objects;

import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.refusal.interfaces.Refusal;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class RefusalImpl implements Refusal {

  @JsonView(Views.Public.class)
  private int idRefusal;
  @JsonView(Views.Public.class)
  private Member member;
  @JsonView(Views.Public.class)
  private String text;
  @JsonView(Views.Public.class)
  private int version;

  @Override
  public int getIdRefusal() {
    return idRefusal;
  }

  @Override
  public void setIdRefusal(int idRefusal) {
    this.idRefusal = idRefusal;
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
  public String getText() {
    return text;
  }

  @Override
  public void setText(String text) {
    this.text = text;
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
    return "RefusalImpl{"
        + "idRefusal=" + idRefusal
        + ", member=" + member
        + ", text='" + text + '\''
        + '}';
  }
}
