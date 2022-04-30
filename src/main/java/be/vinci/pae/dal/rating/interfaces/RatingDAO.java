package be.vinci.pae.dal.rating.interfaces;

import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import java.util.List;

public interface RatingDAO {

  boolean ratingExist(RatingDTO ratingDTO);

  boolean evaluate(RatingDTO ratingDTO);

  /**
   * Get all ratings of a specified member.
   *
   * @param idMember the member's id
   * @return the list of member's ratings
   */
  List<RatingDTO> getAllRatingsOfMember(int idMember);
}
