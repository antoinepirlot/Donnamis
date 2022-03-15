package be.vinci.pae.ihm.interest;

import be.vinci.pae.biz.interest.InterestUCC;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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

  @POST
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public int markInterest(@PathParam("id") int id_offer, JsonNode json) {
    if (!interestUCC.offerExist(id_offer)) {
      throw new WebApplicationException(Response.status(Status.NOT_FOUND)
          .entity("offer not found").type("text/plain").build());
    }

    if (!json.hasNonNull("id_member")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("id_member required").type("text/plain").build());
    }

    int id_member = json.get("id_member").asInt();

    if (!interestUCC.memberExist(id_member)) {
      throw new WebApplicationException(Response.status(Status.NOT_FOUND)
          .entity("member not found").type("text/plain").build());
    }

    int res = interestUCC.markInterest(id_member, id_offer, false);
    if (res == -1) {
      throw new WebApplicationException(Response.status(Status.CONFLICT)
          .entity("already in the db").type("text/plain").build());
    }
    return res;
  }
}
