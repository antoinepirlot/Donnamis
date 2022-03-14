package be.vinci.pae.ihm.interest;

import be.vinci.pae.biz.interest.InterestUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("interests")
public class InterestResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();

  @Inject
  private InterestUCC interestUCC;

  @PUT
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void markInterest(@PathParam("id") int id) {

  }
}
