package be.vinci.pae.ihm.offer;

import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.filter.utils.Json;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Singleton
@Path("offers")
public class OfferResource {

  private final Json<OfferDTO> jsonUtil = new Json<>(OfferDTO.class);
  @Inject
  private OfferUCC offerUCC;

  /**
   * Asks UCC to add an offer into the database.
   *
   * @param offerDTO the offer to add into the database
   */
  @POST
  @Path("add_offer")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void addOffer(OfferDTO offerDTO) throws SQLException {
    // Get and check credentials
    if (offerDTO == null || offerDTO.getIdItem() < 1) {
      String message = "";
      if (offerDTO == null) {
        message = "offerDTO is null.";
      } else {
        message = "item id = " + offerDTO.getIdItem() + " and is not greater than 0.";
      }
      throw new WrongBodyDataException(message);
    }
    //Try to create an offer
    if (!offerUCC.createOffer(offerDTO)) {
      String message = "Add an existing offer.";
      throw new ConflictException(message);
    }

  }

  /**
   * Method that get all the latest items offered.
   *
   * @return the list of lastest offers if there's at least one offer, otherwise null
   */
  @GET
  @Path("latest_offers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<OfferDTO> getLatestOffers() throws SQLException {
    List<OfferDTO> listOfferDTO = offerUCC.getLatestOffers();
    if (listOfferDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    return this.jsonUtil.filterPublicJsonViewAsList(listOfferDTO);
  }


  /**
   * Method that get all the items offered.
   *
   * @return the list of all offers if there's at least one offer, otherwise null
   */
  @GET
  @Path("all_offers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<OfferDTO> getAllOffers() throws SQLException {
    List<OfferDTO> listOfferDTO = offerUCC.getAllOffers();
    if (listOfferDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    return this.jsonUtil.filterPublicJsonViewAsList(listOfferDTO);
  }

}
