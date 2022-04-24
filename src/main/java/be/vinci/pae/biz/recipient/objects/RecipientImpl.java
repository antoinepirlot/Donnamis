package be.vinci.pae.biz.recipient.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.member.interfaces.Member;
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
  public Item getItem() {
    return item;
  }

  @Override
  public void setItem(Item item) {
    this.item = item;
  }

  @Override
  public Member getMember() {
    return member;
  }

  @Override
  public void setMember(Member member) {
    this.member = member;
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
  public String toString() {
    return "RecipientImpl{"
        + "id=" + id
        + ", item=" + item
        + ", member=" + member
        + ", received='" + received + '\''
        + '}';
  }
}
