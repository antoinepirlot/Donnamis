package be.vinci.pae.dal.rating.interfaces;

import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import java.sql.SQLException;
import java.util.List;

public interface RatingDAO {

  boolean ratingExist(RatingDTO ratingDTO) throws SQLException;

  boolean evaluate(RatingDTO ratingDTO) throws SQLException;

  /**
   * Get all ratings of a specified member.
   *
   * @param idMember the member's id
   * @return the list of member's ratings
   */
  List<RatingDTO> getAllRatingsOfMember(int idMember);
}
