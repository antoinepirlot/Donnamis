package be.vinci.pae.ihm;

import be.vinci.pae.biz.ItemDTO;
import be.vinci.pae.biz.ItemUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Singleton
@Path("items")
public class ItemResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private ItemUCC itemUCC;

  /*
  Method that get all the latest items offered
   */
  @GET
  @Path("latest_items")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getLatestItems() {
    List<ItemDTO> listItemDTO = itemUCC.getLatestItems();
    if (listItemDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }

    //Convert to ObjectNode
    try {
      return listItemDTO;
    } catch (Exception e) {
      System.out.println("Unable to create list of the latest items");
      return null;
    }
  }


  /*
  Method that get all the items offered
   */
  @GET
  @Path("all_items")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllItems() {
    List<ItemDTO> listItemDTO = itemUCC.getAllItems();
    if (listItemDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }

    //Convert to ObjectNode
    try {
      return listItemDTO;
    } catch (Exception e) {
      System.out.println("Unable to create list of the latest items");
      return null;
    }
  }

  
}
