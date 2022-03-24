package be.vinci.pae.ihm.offer;

import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.ihm.filter.Authorize;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Singleton
@Path("offers")
public class OfferResource {

  @Inject
  private OfferUCC offerUCC;

  /**
   * Asks UCC to add an offer into the database.
   * @param offerDTO the offer to add into the database
   */
  @POST
  @Path("add_offer")
  @Consumes(MediaType.APPLICATION_JSON)
  @Authorize
  public void addOffer(OfferDTO offerDTO) {
    // Get and check credentials
    if (offerDTO.getTimeSlot() == null
    ) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("Some information required").type("text/plain").build());
    }

    //Try to create an offer
    if (!offerUCC.createOffer(offerDTO)) {
      throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
          .entity("this resource already exists").type(MediaType.TEXT_PLAIN)
          .build());
    }

  }


  @GET
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public OfferDTO getOffer(@PathParam("id") int id) {
    if (offerUCC.getOneOffer(id) == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    return offerUCC.getOneOffer(id);
  }

  /**
   * Method that get all the latest items offered.
   * @return the list of lastest offers if there's at least one offer, otherwise null
   */
  @GET
  @Path("latest_offers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<OfferDTO> getLatestOffers() {
    List<OfferDTO> listOfferDTO = offerUCC.getLatestOffers();
    if (listOfferDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }

    //Convert to ObjectNode
    try {
      return listOfferDTO;
    } catch (Exception e) {
      System.out.println("Unable to create list of the latest items");
      return null;
    }
  }


  /**
   * Method that get all the items offered.
   * @return the list of all offers if there's at least one offer, otherwise null
   */
  @GET
  @Path("all_offers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Authorize
  public List<OfferDTO> getAllOffers() {
    List<OfferDTO> listOfferDTO = offerUCC.getAllOffers();
    if (listOfferDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }

    //Convert to ObjectNode
    try {
      return listOfferDTO;
    } catch (Exception e) {
      System.out.println("Unable to create list of all the items");
      return null;
    }
  }

}
