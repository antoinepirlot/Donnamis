package be.vinci.pae.ihm;

import be.vinci.pae.biz.OfferDTO;
import be.vinci.pae.biz.OfferUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

}
