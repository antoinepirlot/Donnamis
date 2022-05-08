package be.vinci.pae.biz.recipient.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.recipient.interfaces.Recipient;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonView;

public class RecipientImpl implements Recipient {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private Item item;
  @JsonView(Views.Public.class)
  private Member member;
  @JsonView(Views.Public.class)
  private String received;
  @JsonView(Views.Public.class)
  private int version;

  public RecipientImpl() {

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
  public ItemDTO getItem() {
    return item;
  }

  @Override
  public void setItem(ItemDTO item) {
    this.item = (Item) item;
  }

  @Override
  public MemberDTO getMember() {
    return member;
  }

  @Override
  public void setMember(MemberDTO memberDTO) {
    this.member = (Member) memberDTO;
  }

  @Override
  public String getReceived() {
    return received;
  }

  @Override
  public void setReceived(String received) {
    this.received = received;
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
    return "RecipientImpl{"
        + "id=" + id
        + ", item=" + item
        + ", member=" + member
        + ", received='" + received + '\''
        + '}';
  }
}
