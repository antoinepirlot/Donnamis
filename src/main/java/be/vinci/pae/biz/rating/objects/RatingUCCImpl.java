package be.vinci.pae.biz.rating.objects;

import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.rating.interfaces.RatingUCC;
import be.vinci.pae.dal.rating.interfaces.RatingDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ForbiddenException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import jakarta.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class RatingUCCImpl implements RatingUCC {

  @Inject
  private RatingDAO ratingDAO;
  @Inject
  private MemberUCC memberUCC;
  @Inject
  private DALServices dalServices;

  @Override
  public void evaluate(RatingDTO ratingDTO) {
    if (ratingDTO.getItem().getMember().getId() == ratingDTO.getMember().getId()) {
      throw new ForbiddenException("This user can't add a rating for his own item");
    }
    if (this.ratingExist(ratingDTO)) {
      throw new ConflictException("Rating already exist");
    }
    try {
      dalServices.start();
      boolean evaluated = ratingDAO.evaluate(ratingDTO);
      dalServices.commit();
      if (!evaluated) {
        throw new FatalException("Rating not add");
      }
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public List<RatingDTO> getAllRatingsOfMember(int idMember) {
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new WrongBodyDataException("The member : " + idMember + " doesn't exist");
    }
    try {
      this.dalServices.start();
      List<RatingDTO> ratingDTOList = this.ratingDAO.getAllRatingsOfMember(idMember);
      this.dalServices.commit();
      if (ratingDTOList == null) {
        throw new ObjectNotFoundException("No ratings for the member " + idMember);
      }
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
