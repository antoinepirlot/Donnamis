package be.vinci.pae.biz.rating.objects;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.rating.interfaces.Rating;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class RatingImpl implements Rating {

  @JsonView(Views.Public.class)
  private int id;
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
  public void setItem(ItemDTO itemDTO) {
    this.item = (Item) itemDTO;
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
  public MemberDTO getMember() {
    return member;
  }

  @Override
  public void setMember(MemberDTO memberDTO) {
    this.member = (Member) memberDTO;
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
