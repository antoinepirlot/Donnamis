package be.vinci.pae.ihm.refusal;

import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalUCC;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.filter.utils.Json;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("refusals")
@AuthorizeMember
public class RefusalResource {

  private final Json<RefusalDTO> json = new Json<>(RefusalDTO.class);
  @Inject
  private RefusalUCC refusalUCC;

  /**
   * Get the refusal message of the member identified by its id.
   *
   * @param username the member's username
   * @return the refusal message
   */
  @GET
  @Path("{idMember}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public RefusalDTO getRefusal(@PathParam("idMember") String username) {
    if (username == null || username.isBlank()) {
      throw new WrongBodyDataException("username is lower than 1.");
    }
    RefusalDTO refusalDTO = this.refusalUCC.getRefusal(username);
    System.out.println(refusalDTO);
    if (refusalDTO == null) {
      String message = "Refusal information for the member " + username + " not found.";
      throw new ObjectNotFoundException(message);
    }
    return this.json.filterPublicJsonView(refusalDTO);
  }

}
