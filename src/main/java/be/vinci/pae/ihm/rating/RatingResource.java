package be.vinci.pae.ihm.rating;

import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.rating.interfaces.RatingUCC;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Path("ratings")
public class RatingResource {

  @Inject
  private RatingUCC ratingUCC;

  @Inject
  private MemberUCC memberUCC;

  /////////////////////////////////////////////////////////
  ///////////////////////GET///////////////////////////////
  /////////////////////////////////////////////////////////

  @GET
  @Path("all/{idMember}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<RatingDTO> getAllRatingsOfMember(@PathParam("idMember") int idMember) {
    if (idMember < 1) {
      throw new WrongBodyDataException("idMember is lower than 1");
    }
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new WrongBodyDataException("The member : " + idMember + " doesn't exist");
    }
    List<RatingDTO> ratingDTOList = this.ratingUCC.getAllRatingsOfMember(idMember);
    if (ratingDTOList == null) {
      throw new ObjectNotFoundException("No ratings for the member " + idMember);
    }
    return ratingDTOList;
  }

  /////////////////////////////////////////////////////////
  ///////////////////////POST//////////////////////////////
  /////////////////////////////////////////////////////////
  @POST
  @Path("")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public boolean evaluateItem(RatingDTO ratingDTO) {
    //Verify the content of the request
    if (ratingDTO == null
        || ratingDTO.getRating() < 1 || ratingDTO.getRating() > 5
        || ratingDTO.getItem() == null || ratingDTO.getItem().getId() < 1
        || ratingDTO.getMember() == null || ratingDTO.getMember().getId() < 1
        || ratingDTO.getText() == null || ratingDTO.getText().isBlank()) {
      throw new WrongBodyDataException("Wrong Body Request");
    }

    if (this.ratingUCC.ratingExist(ratingDTO)) {
      throw new ConflictException("Rating already exist");
    }

    if (!ratingUCC.evaluate(ratingDTO)) {
      throw new FatalException("Rating not add");
    }
    return true;
  }
}
