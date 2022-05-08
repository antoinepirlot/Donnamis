package be.vinci.pae.biz.rating.interfaces;

import java.util.List;

public interface RatingUCC {

  /**
   * Verify if the rating already exist into the DB.
   *
   * @param ratingDTO the rating to verify
   * @return true if already exist or false if not
   */
  boolean ratingExist(RatingDTO ratingDTO);

  /**
   * Adds the evaluation of an item.
   *
   * @param ratingDTO the new rating
   */
  void evaluate(RatingDTO ratingDTO);

  /**
   * Get all ratings of a specified member.
   *
   * @param idMember the member's id
   * @return the list of member's ratings
   */
  List<RatingDTO> getAllRatingsOfMember(int idMember);
}
