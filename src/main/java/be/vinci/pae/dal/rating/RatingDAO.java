package be.vinci.pae.dal.rating;

import be.vinci.pae.biz.rating.RatingDTO;
import java.sql.SQLException;

public interface RatingDAO {

  boolean ratingExist(RatingDTO ratingDTO) throws SQLException;

  boolean evaluate(RatingDTO ratingDTO) throws SQLException;

}
