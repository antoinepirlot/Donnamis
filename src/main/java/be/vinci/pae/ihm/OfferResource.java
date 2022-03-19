package be.vinci.pae.ihm;

import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

@Singleton
@Path("offers")
public class OfferResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private OfferUCC offerUCC;

  @POST
  @Path("add_offer")
  @Consumes(MediaType.APPLICATION_JSON)
  public void add_offer(OfferDTO offerDTO) {
    // Get and check credentials
    if (offerDTO.getTime_slot() == null
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

  /*
  Method that get all the latest items offered
   */
  @GET
  @Path("latest_offers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
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


  /*
  Method that get all the items offered
   */
  @GET
  @Path("all_offers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
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
