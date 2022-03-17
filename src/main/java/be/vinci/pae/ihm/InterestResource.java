package be.vinci.pae.ihm;

import be.vinci.pae.biz.interfaces.interest.InterestUCC;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("interests")
public class InterestResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private InterestUCC interestUCC;

  /**
   * Mark the interest in an offer.
   *
   * @param id_offer    the id of the offer
   * @param call_wanted true if call wanted false if not
   * @param json        contains the id_member
   * @return 1 if interest good added -1 if not
   */
  @POST
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public int markInterest(@PathParam("id") int id_offer,
      @QueryParam("call_wanted") boolean call_wanted, JsonNode json) {

    //Verify if the offer already exist
    if (!interestUCC.offerExist(id_offer)) {
      throw new WebApplicationException(Response.status(Status.NOT_FOUND)
          .entity("offer not found").type("text/plain").build());
    }

    //Verify the content of the request
    if (!json.hasNonNull("id_member")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("id_member required").type("text/plain").build());
    }
    int id_member = json.get("id_member").asInt();

    //Verify if the member already exist
    if (!interestUCC.memberExist(id_member)) {
      throw new WebApplicationException(Response.status(Status.NOT_FOUND)
          .entity("interest already exist").type("text/plain").build());
    }

    //Verify if the interest already exist
    if (interestUCC.interestExist(id_offer, id_member)) {
      throw new WebApplicationException(Response.status(Status.NOT_FOUND)
          .entity("member not found").type("text/plain").build());
    }

    //Add the interest
    int res = interestUCC.markInterest(id_member, id_offer, call_wanted);
    if (res == -1) {
      throw new WebApplicationException(Response.status(Status.CONFLICT)
          .entity("error ").type("text/plain").build());
    }
    return res;
  }
}
