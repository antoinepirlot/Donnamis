
package be.vinci.pae.biz.rating;

import be.vinci.pae.biz.item.interfaces.Item;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.rating.objects.RatingImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = RatingImpl.class)
public interface RatingDTO {

  Item getItem();

  void setItem(Item item);

  int getRating();

  void setRating(int rating);

  String getText();

  void setText(String text);

  Member getMember();

  void setMember(Member member);

  int getVersion();

  void setVersion(int version);
}