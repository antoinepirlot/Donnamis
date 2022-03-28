package be.vinci.pae.ihm.item;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.utils.Json;
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
import java.util.ArrayList;
import java.util.List;

@Singleton
@Path("items")
public class ItemResource {

  private final Json<ItemDTO> jsonUtil = new Json<>(ItemDTO.class);
  @Inject
  private ItemUCC itemUCC;
  @Inject
  private OfferUCC offerUCC;

  /**
   * Method that get all the latest offered items.
   *
   * @return a list of the latest offered items
   */
  @GET
  @Path("latest_items")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<ItemDTO> getLatestItems() {
    List<ItemDTO> listItemDTO = itemUCC.getLatestItems();
    if (listItemDTO == null) {
      throw new WebApplicationException("Ressource not found", Status.NOT_FOUND);
    }

    //Convert to ObjectNode
    try {
      return this.jsonUtil.filterPublicJsonViewAsList(listItemDTO);
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
  @AuthorizeMember
  public List<ItemDTO> getAllItems() {
    List<ItemDTO> listItemDTO = itemUCC.getAllItems();
    if (listItemDTO == null) {
      throw new WebApplicationException("Ressource not found", Status.NOT_FOUND);
    }
    for(ItemDTO itemDTO : listItemDTO) {
      this.offerUCC.getAllOffersOf(itemDTO);
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
  @AuthorizeMember
  public List<ItemDTO> getAllOfferedItems() {
    System.out.println("Get all offered items");
    List<ItemDTO> itemDTOList = this.itemUCC.getAllOfferedItems();
    for(ItemDTO itemDTO : itemDTOList) {
      this.offerUCC.getAllOffersOf(itemDTO);
    }
    return itemDTOList;
  }

  /**
   * Checks the item's integrity and asks the UCC to add it into the database.
   *
   * @param itemDTO to add into the database
   */
  @POST
  @Path("offer")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void addItem(ItemDTO itemDTO) {
    if (itemDTO == null
        || itemDTO.getItemDescription() == null || itemDTO.getItemDescription().equals("")
        || itemDTO.getItemType() == null || itemDTO.getMember() == null
        || itemDTO.getMember().getId() < 1
        || itemDTO.getTitle() == null || itemDTO.getTitle().equals("")
    ) {
      throw new WebApplicationException("Wrong item body", Status.BAD_REQUEST);
    }
    if (!this.itemUCC.addItem(itemDTO)) {
      String message = "The items can't be added to the db due to a unexpected error";
      throw new WebApplicationException(message, Status.BAD_REQUEST);
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
  @AuthorizeMember
  public ItemDTO cancelOffer(@PathParam("id") int id) {
    if (itemUCC.getOneItem(id) == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);
    }
    ItemDTO itemDTO = itemUCC.cancelOffer(id);
    return this.jsonUtil.filterPublicJsonView(itemDTO);
  }

  /**
   * Method that get all items by the member id.
   *
   * @param id the member's id
   * @return a list of all items
   */
  @GET
  @Path("all_items/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<ItemDTO> getAllItemsByMemberId(@PathParam("id") int id) {
    List<ItemDTO> listItemDTO = itemUCC.getAllItemsByMemberId(id);
    if (listItemDTO == null) {
      throw new WebApplicationException("Ressource not found", Status.NOT_FOUND);
    }
    for(ItemDTO itemDTO : listItemDTO) {
      this.offerUCC.getAllOffersOf(itemDTO);
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
   * Asks UCC to get one offer identified by its id.
   *
   * @param id the item's id
   * @return the offer if it exists, otherwise throws a web application exception
   */
  @GET
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public ItemDTO getItem(@PathParam("id") int id) {
    ItemDTO itemDTO = itemUCC.getOneItem(id);
    if (itemDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    this.offerUCC.getAllOffersOf(itemDTO);
    return this.jsonUtil.filterPublicJsonView(itemDTO);
  }


}
