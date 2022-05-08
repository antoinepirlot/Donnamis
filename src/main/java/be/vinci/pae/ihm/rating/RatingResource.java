package be.vinci.pae.ihm.rating;

import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.rating.interfaces.RatingUCC;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.filter.utils.Json;
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
@AuthorizeMember
public class RatingResource {

  @Inject
  private RatingUCC ratingUCC;

  private final Json<RatingDTO> json = new Json<>(RatingDTO.class);

  /////////////////////////////////////////////////////////
  ///////////////////////GET///////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Get all ratings form a specific member identified by its id.
   * @param idMember the member's id to get ratings
   * @return the list of member's ratings
   */
  @GET
  @Path("all/{idMember}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<RatingDTO> getAllRatingsOfMember(@PathParam("idMember") int idMember) {
    if (idMember < 1) {
      throw new WrongBodyDataException("idMember is lower than 1");
    }
    List<RatingDTO> ratingDTOList = this.ratingUCC.getAllRatingsOfMember(idMember);
    return this.json.filterPublicJsonViewAsList(ratingDTOList);
  }

  /////////////////////////////////////////////////////////
  ///////////////////////POST//////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Add an evaluation to the item identified by its id.
   * @param ratingDTO the rating to add
   */
  @POST
  @Path("")
  @Consumes(MediaType.APPLICATION_JSON)
  public void evaluateItem(RatingDTO ratingDTO) {
    //Verify the content of the request
    if (ratingDTO == null
        || ratingDTO.getRating() < 1 || ratingDTO.getRating() > 5
        || ratingDTO.getItem() == null || ratingDTO.getItem().getId() < 1
        || ratingDTO.getMember() == null || ratingDTO.getMember().getId() < 1
        || ratingDTO.getItem().getMember() == null
        || ratingDTO.getItem().getMember().getId() < 1
        || ratingDTO.getText() == null || ratingDTO.getText().isBlank()) {
      throw new WrongBodyDataException("Wrong Body Request");
    }
    ratingUCC.evaluate(ratingDTO);
  }
}
