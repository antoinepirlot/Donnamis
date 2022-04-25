package be.vinci.pae.biz.rating;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class RatingImpl implements Rating {

  @JsonView(Views.Public.class)
  private Item item;
  @JsonView(Views.Public.class)
  private int rating;
  @JsonView(Views.Public.class)
  private String text;
  @JsonView(Views.Public.class)
  private Member member;
  @JsonView(Views.Public.class)
  private int version;

  public RatingImpl() {

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
  public int getRating() {
    return rating;
  }

  @Override
  public void setRating(int rating) {
    this.rating = rating;
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
  public Member getMember() {
    return member;
  }

  @Override
  public void setMember(Member member) {
    this.member = member;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
  }
}
