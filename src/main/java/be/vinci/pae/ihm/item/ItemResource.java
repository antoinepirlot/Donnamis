package be.vinci.pae.ihm.item;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@Singleton
@Path("items")
public class ItemResource {

  @Inject
  private ItemUCC itemUCC;

  /**
   * Method that get all the latest offered items.
   *
   * @return a list of the latest offered items
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


  /**
   * Method that get all items.
   *
   * @return a list of all items
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

  /**
   * Gets all offered items.
   *
   * @return a list of all offered items
   */
  @GET
  @Path("all_offered_items")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllOfferedItems() {
    System.out.println("Get all offered items");
    return this.itemUCC.getAllOfferedItems();
  }

  /**
   * Checks the item's integrity and asks the UCC to add it into the database.
   *
   * @param itemDTO to add into the database
   */
  @POST
  @Path("offer")
  @Consumes(MediaType.APPLICATION_JSON)
  public void addItem(ItemDTO itemDTO) {
    if (itemDTO == null
        || itemDTO.getItemDescription() == null || itemDTO.getItemDescription().equals("")
        || itemDTO.getItemType() == null || itemDTO.getMember() == null
        || itemDTO.getTitle() == null || itemDTO.getTitle().equals("")
    ) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity("Wrong item body")
          .type(MediaType.TEXT_PLAIN_TYPE)
          .build());
    }
    if (!this.itemUCC.addItem(itemDTO)) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity("The items can't be added to the db due to a unexpected error")
          .type(MediaType.TEXT_PLAIN_TYPE)
          .build());
    }
  }

  /**
   * Asks the UCC to cancel the item's offer.
   *
   * @param id the item's id
   * @return the canceled item
   */
  @PUT
  @Path("cancel/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ItemDTO cancelOffer(@PathParam("id") int id) {
    if (itemUCC.getOneItem(id) == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Item not found").type("text/plain").build());
    }
    return itemUCC.cancelOffer(id);
  }
}
