package be.vinci.pae.ihm.rating;

import be.vinci.pae.biz.rating.RatingDTO;
import be.vinci.pae.biz.rating.RatingUCC;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.rmi.UnexpectedException;
import java.sql.SQLException;

@Singleton
@Path("ratings")
public class RatingResource {

  @Inject
  private RatingUCC ratingUCC;

  @POST
  @Path("")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public boolean evaluateItem(RatingDTO ratingDTO) throws SQLException, UnexpectedException {
    //Verify the content of the request
    if (ratingDTO == null || ratingDTO.getRating() < 1 || ratingDTO.getRating() > 5
        || ratingDTO.getItem() == null || ratingDTO.getMember() == null
        || ratingDTO.getText().isBlank()) {
      throw new WrongBodyDataException("Wrong Body Request");
    }

    if (this.ratingUCC.ratingExist(ratingDTO)) {
      throw new ConflictException("Rating already exist");
    }

    if (!ratingUCC.evaluate(ratingDTO)) {
      throw new UnexpectedException("Rating not add");
    }

    return true;
  }
}
