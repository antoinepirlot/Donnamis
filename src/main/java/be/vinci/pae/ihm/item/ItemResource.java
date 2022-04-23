package be.vinci.pae.ihm.item;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.exceptions.webapplication.ForbiddenException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeAdmin;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.filter.utils.Json;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
import java.rmi.UnexpectedException;
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
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllItemsByOfferStatus(@PathParam("offer_status") String offerStatus)
      throws SQLException {
    if (offerStatus == null || offerStatus.isBlank()
        || (!offerStatus.equals("donated")
            && !offerStatus.equals("assigned")
            && !offerStatus.equals("cancelled")
            && !offerStatus.equals("given")
        )
    ) {
      throw new WrongBodyDataException("Offer status " + offerStatus + " is not valid.");
    }
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
  public List<ItemDTO> getAllItemsByMemberIdAndOfferStatus(@PathParam("idMember") int idMember)
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
   * Get all donated items of the member identified by its id.
   * @param idMember the member's id
   * @param offerStatus the item's offer status
   * @return the list of member's donated items
   * @throws SQLException if an error occurs while getting items
   */
  @GET
  @Path("{idMember}/{offerStatus}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<ItemDTO> getAllItemsByMemberIdAndOfferStatus(@PathParam("idMember") int idMember,
      @PathParam("offerStatus") String offerStatus) throws SQLException {
    if (idMember < 1
        || offerStatus == null || offerStatus.isBlank()
        || (!offerStatus.equals("donated")
            && !offerStatus.equals("given")
        )
    ) {
      throw new WrongBodyDataException("idMember or offer status are not valid.");
    }

    if(offerStatus.equals("donated")) {
      return this.jsonUtil
          .filterPublicJsonViewAsList(itemUCC.getMemberItemsByOfferStatus(idMember, offerStatus));
    }
    return this.jsonUtil.filterPublicJsonViewAsList(this.itemUCC.getMemberReceivedItems(idMember));
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

  /**
   * This method get items that have been assigned to this member identified by its id.
   *
   * @param idMember the member's id
   * @return the list of assigned items of the member
   * @throws SQLException if an error occurs in the UCC method
   */
  @GET
  @Path("assigned_items/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<ItemDTO> getAssignedItems(@PathParam("id") int idMember) throws SQLException {
    if (idMember < 0) {
      throw new WrongBodyDataException("idMember < 0 for get assigned items");
    }
    return this.jsonUtil.filterPublicJsonViewAsList(this.itemUCC.getAssignedItems(idMember));
  }

  /**
   * Count the number of items with a specific offer status for the member with the idMember.
   * @param idMember the member's id
   * @param offerStatus the offer status can only be "donated" or "given"
   * @return the number of items that match offer status
   * @throws SQLException if an error occurs while counting.
   * @throws WrongBodyDataException if the offer status is invalid
   * @throws ObjectNotFoundException if the member doesn't exist
   */
  @GET
  @Path("count/{idMember}/{offer_status}")
  @Consumes(MediaType.TEXT_PLAIN)
  @AuthorizeAdmin
  public int countNumberOfItemsByOfferStatus(@PathParam("idMember") int idMember,
      @PathParam("offer_status") String offerStatus) throws SQLException {
    if (idMember < 1
        || offerStatus == null || offerStatus.isBlank()
        || (!offerStatus.equals("donated") && !offerStatus.equals("given"))
    ) {
      throw new WrongBodyDataException("Wrong offerStatus is invalid.");
    }
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("The member "+idMember+" doesn't exist.");
    }
    return this.itemUCC.countNumberOfItemsByOfferStatus(idMember, offerStatus);
  }

  /**
   * Count the number of items that have been received or not by the member.
   * If received is true that means the item has been received by the member.
   * If received is false that means the member marked his interest in the item but
   * the member who offers the item marked the member has never received the item.
   * @param idMember the member's id
   * @param received true if the item has been received by the member
   *                 false if the member had marked its interest but never take the item.
   * @return the number of the number of items received or not received
   * @throws SQLException if an error occurs while counting items
   * @throws WrongBodyDataException if the id member is lower than 1
   * @throws ObjectNotFoundException if the member doesn't exist in the database
   */
  @GET
  @Path("count_assigned_items/{idMember}/{received}")
  @AuthorizeAdmin
  public int countNumberOfReceivedOrNotReceivedItems(@PathParam("idMember") int idMember,
      @PathParam("received") boolean received) throws SQLException {
    if (idMember < 1) {
      throw new WrongBodyDataException("idMember is lower than 1");
    }
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("The member " + idMember + " doesn't exist.");
    }
    return this.itemUCC.countNumberOfReceivedOrNotReceivedItems(idMember, received);
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
  @Path("")
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
  ////////////////////////PUT//////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Mark the item identified by its id as given.
   *
   * @param itemDTO the item to update
   * @throws SQLException        if an error occurs while updating item
   * @throws UnexpectedException if marking item as given returned false
   */
  @PUT
  @Path("given")
  @Consumes(MediaType.APPLICATION_JSON)
  public void markItemAsGiven(ItemDTO itemDTO)
      throws SQLException, UnexpectedException {
    this.checkMarkItem(itemDTO);
    if (!this.itemUCC.markItemAsGiven(itemDTO)) {
      throw new UnexpectedException("marking item " + itemDTO.getId() + " as given failed.");
    }
  }

  /**
   * Mark the item identified by its id as donated and update recipient to not received.
   *
   * @param itemDTO the item to update
   * @throws SQLException        if an error occurs while updating item
   * @throws UnexpectedException if marking item as given returned false
   */
  @PUT
  @Path("not_given")
  @Consumes(MediaType.APPLICATION_JSON)
  public void markItemAsNotGiven(ItemDTO itemDTO)
      throws SQLException, UnexpectedException {
    this.checkMarkItem(itemDTO);
    if (!this.itemUCC.markItemAsNotGiven(itemDTO)) {
      throw new UnexpectedException("marking item " + itemDTO.getId() + " as not given failed.");
    }
  }

  /////////////////////////////////////////////////////////
  ///////////////////////DELETE////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Asks the UCC to cancel the item's offer.
   *
   * @param id the item's id
   * @return the canceled item
   */
  @DELETE
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public ItemDTO cancelOffer(@PathParam("id") int id) throws SQLException {
    if (itemUCC.getOneItem(id) == null) {
      throw new ObjectNotFoundException("Item not found");
    }
    ItemDTO itemDTO = itemUCC.cancelItem(id);
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

  /**
   * Check itemDTO for mark item as {offerStatus}.
   *
   * @param itemDTO the item to check
   * @throws SQLException if an error occurs while getting item from database
   */
  private void checkMarkItem(ItemDTO itemDTO) throws SQLException {
    if (itemDTO == null
        || itemDTO.getId() < 1
        || itemDTO.getMember() == null || itemDTO.getMember().getId() < 1
    ) {
      throw new WrongBodyDataException("idItm is lower than 1.");
    }
    ItemDTO existingItem = this.itemUCC.getOneItem(itemDTO.getId());
    if (existingItem == null) {
      throw new ObjectNotFoundException("The item " + itemDTO.getId() + " doesn't exist.");
    }
    if (existingItem.getMember().getId() != itemDTO.getMember().getId()) {
      throw new WrongBodyDataException("The member's id and item's member's id are not associated");
    }
    if (existingItem.getOfferStatus().equals("given")) {
      throw new ForbiddenException("The item " + itemDTO.getId() + " is already given.");
    }
  }
}
