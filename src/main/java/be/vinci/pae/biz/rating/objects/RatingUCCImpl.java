package be.vinci.pae.biz.rating.objects;

import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.rating.interfaces.RatingUCC;
import be.vinci.pae.dal.rating.interfaces.RatingDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class RatingUCCImpl implements RatingUCC {

  @Inject
  private RatingDAO ratingDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public boolean evaluate(RatingDTO ratingDTO) {
    try {
      dalServices.start();
      boolean isDone = ratingDAO.evaluate(ratingDTO);
      dalServices.commit();
      return isDone;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<RatingDTO> getAllRatingsOfMember(int idMember) {
    try {
      this.dalServices.start();
      List<RatingDTO> ratingDTOList = this.ratingDAO.getAllRatingsOfMember(idMember);
      this.dalServices.commit();
      return ratingDTOList;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean ratingExist(RatingDTO ratingDTO) {
    try {
      dalServices.start();
      boolean ratingExist = this.ratingDAO.ratingExist(ratingDTO);
      dalServices.commit();
      return ratingExist;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

}
