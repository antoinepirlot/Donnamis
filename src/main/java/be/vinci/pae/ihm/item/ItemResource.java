package be.vinci.pae.ihm.item;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeAdmin;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.filter.utils.Json;
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
import java.sql.SQLException;
import java.util.List;

@Singleton
@Path("items")
public class ItemResource {

  private final Json<ItemDTO> jsonUtil = new Json<>(ItemDTO.class);
  @Inject
  private ItemUCC itemUCC;
  @Inject
  private OfferUCC offerUCC;
  @Inject
  private MemberUCC memberUCC;

  /////////////////////////////////////////////////////////
  ///////////////////////GET///////////////////////////////
  /////////////////////////////////////////////////////////
  @GET
  @Path("all_items")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<ItemDTO> getAllItems() throws SQLException {
    return this.getAllItems(null, -1);
  }

  /**
   * Method that get all items.
   *
   * @return a list of all items
   */
  @GET
  @Path("all_items/{offer_status}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllItemsByOfferStatus(@PathParam("offer_status") String offerStatus)
      throws SQLException {
    return this.getAllItems(offerStatus, -1);
  }

  /**
   * Method that get all items by the member id.
   *
   * @param idMember the member's id
   * @return a list of all items
   */
  @GET
  @Path("member_items/{idMember}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<ItemDTO> getAllItemsByMemberId(@PathParam("idMember") int idMember)
      throws SQLException {
    if (idMember < 1) {
      throw new WrongBodyDataException("The idMember must be grater than 1");
    }
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("This member doesn't exists.");
    }
    return this.getAllItems(null, idMember);
  }

  /**
   * Asks UCC to get one offer identified by its id.
   *
   * @param id the item's id
   * @return the offer if it exists, otherwise throws a web application exception
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public ItemDTO getItem(@PathParam("id") int id) throws SQLException {
    ItemDTO itemDTO = itemUCC.getOneItem(id);
    if (itemDTO == null) {
      throw new WebApplicationException(
          Response.status(Response.Status.NOT_FOUND).entity("Ressource not found")
              .type("text/plain").build());
    }
    this.offerUCC.getAllOffersOf(itemDTO);
    System.out.println(itemDTO);
    return this.jsonUtil.filterPublicJsonView(itemDTO);
  }

  /////////////////////////////////////////////////////////
  ///////////////////////POST//////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Checks the item's integrity and asks the UCC to add it into the database.
   *
   * @param itemDTO to add into the database
   */
  @POST
  @Path("offer")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void addItem(ItemDTO itemDTO) throws SQLException {
    System.out.println(itemDTO.getOfferList());
    if (itemDTO.getItemDescription() == null || itemDTO.getItemDescription().isBlank()
        || itemDTO.getItemType() == null || itemDTO.getItemType().getItemType() == null
        || itemDTO.getItemType().getItemType().isBlank() || itemDTO.getMember() == null
        || itemDTO.getMember().getId() < 1 || itemDTO.getTitle() == null || itemDTO.getTitle()
        .isBlank() || itemDTO.getLastOfferDate() == null || itemDTO.getOfferList().get(0) == null) {
      throw new WebApplicationException("Wrong item body", Status.BAD_REQUEST);
    }
    int idItem = this.itemUCC.addItem(itemDTO);
    if (idItem == -1) {
      String message = "The items can't be added to the db due to a unexpected error";
      throw new WebApplicationException(message, Status.BAD_REQUEST);
    }
    OfferDTO offerDTO = itemDTO.getOfferList().get(0);
    offerDTO.setIdItem(idItem);
    if (!this.offerUCC.createOffer(offerDTO)) {
      String message = "The offer can't be added to the db due to a unexpected error";
      throw new WebApplicationException(message, Status.BAD_REQUEST);
    }
  }

  /////////////////////////////////////////////////////////
  ///////////////////////PUT///////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Asks the UCC to cancel the item's offer.
   *
   * @param id the item's id
   * @return the canceled item
   */
  @PUT
  @Path("cancel/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public ItemDTO cancelOffer(@PathParam("id") int id) throws SQLException {
    if (itemUCC.getOneItem(id) == null) {
      throw new WebApplicationException("Item not found", Status.NOT_FOUND);
    }
    ItemDTO itemDTO = itemUCC.cancelOffer(id);
    return this.jsonUtil.filterPublicJsonView(itemDTO);
  }

  /////////////////////////////////////////////////////////
  ///////////////////////UTILS/////////////////////////////
  /////////////////////////////////////////////////////////

  private List<ItemDTO> getAllItems(String offerStatus, int idMember) throws SQLException {
    List<ItemDTO> listItemDTO;
    if (idMember > 0) {
      listItemDTO = itemUCC.getAllItemsOfAMember(idMember);
    } else {
      listItemDTO = itemUCC.getAllItems(offerStatus);
    }
    if (listItemDTO == null) {
      throw new WebApplicationException("Ressource not found", Status.NOT_FOUND);
    }
    for (ItemDTO itemDTO : listItemDTO) {
      this.offerUCC.getAllOffersOf(itemDTO);
    }
    return listItemDTO;
  }
}
