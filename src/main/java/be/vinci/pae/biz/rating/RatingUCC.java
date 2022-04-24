package be.vinci.pae.biz.rating;

import java.sql.SQLException;

public interface RatingUCC {

  /**
   * Verify if the rating already exist into the DB.
   *
   * @param ratingDTO the rating to verify
   * @return true if already exist or false if not
   * @throws SQLException SQL Error
   */
  boolean ratingExist(RatingDTO ratingDTO) throws SQLException;

  /**
   * Adds the evaluation of an item.
   *
   * @param ratingDTO the new rating
   * @return true if correctly add into the DB or false if not
   * @throws SQLException SQL Error
   */
  boolean evaluate(RatingDTO ratingDTO) throws SQLException;

}
