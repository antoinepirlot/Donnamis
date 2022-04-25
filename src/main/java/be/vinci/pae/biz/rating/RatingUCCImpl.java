package be.vinci.pae.biz.rating;

import be.vinci.pae.dal.rating.RatingDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class RatingUCCImpl implements RatingUCC {

  @Inject
  private RatingDAO ratingDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public boolean evaluate(RatingDTO ratingDTO) throws SQLException {
    try {
      dalServices.start();
      boolean isDone = ratingDAO.evaluate(ratingDTO);
      dalServices.commit();
      return isDone;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

  @Override
  public boolean ratingExist(RatingDTO ratingDTO) throws SQLException {
    try {
      dalServices.start();
      boolean ratingExist = this.ratingDAO.ratingExist(ratingDTO);
      dalServices.commit();
      return ratingExist;
    } catch (SQLException e) {
      dalServices.rollback();
      throw e;
    }
  }

}
