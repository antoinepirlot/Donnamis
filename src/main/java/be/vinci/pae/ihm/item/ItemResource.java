package be.vinci.pae.ihm.item;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.exceptions.FatalException;
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
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@Singleton
@Path("items")
public class ItemResource {

  private final Json<ItemDTO> jsonUtil = new Json<>(ItemDTO.class);
  @Inject
  private ItemUCC itemUCC;
  @Inject
  private OfferUCC offerUCC;

  /////////////////////////////////////////////////////////
  ///////////////////////GET///////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Get all items from the database.
   *
   * @return a list of all items
   */
  @GET
  @Path("all_items")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<ItemDTO> getAllItems() {
    return this.jsonUtil.filterPublicJsonViewAsList(itemUCC.getAllItems(null));
  }

  /**
   * Method that get all items.
   *
   * @return a list of all items
   */
  @GET
  @Path("all_items/{offer_status}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllItemsByOfferStatus(@PathParam("offer_status") String offerStatus) {
    if (offerStatus == null || offerStatus.isBlank()
        || !offerStatus.equals("donated")
        && !offerStatus.equals("assigned")
        && !offerStatus.equals("cancelled")
        && !offerStatus.equals("given")
    ) {
      throw new WrongBodyDataException("Offer status " + offerStatus + " is not valid.");
    }
    return this.jsonUtil.filterPublicJsonViewAsList(itemUCC.getAllItems(offerStatus));
  }

  /**
   * Get all public items from the database.
   *
   * @return the list of items
   */
  @GET
  @Path("all_items/public")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllPublicItems() {
    return this.jsonUtil.filterPublicJsonViewAsList(this.itemUCC.getAllPublicItems());
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
  public List<ItemDTO> getAllItemsByMemberId(@PathParam("idMember") int idMember) {
    if (idMember < 1) {
      throw new WrongBodyDataException("The idMember must be grater than 1");
    }
    return this.jsonUtil.filterPublicJsonViewAsList(this.itemUCC.getAllItemsOfAMember(idMember));
  }

  /**
   * Get all donated items of the member identified by its id.
   *
   * @param idMember    the member's id
   * @param offerStatus the item's offer status
   * @return the list of member's donated items
   */
  @GET
  @Path("{idMember}/{offerStatus}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<ItemDTO> getAllItemsByMemberId(@PathParam("idMember") int idMember,
      @PathParam("offerStatus") String offerStatus) {
    if (idMember < 1
        || offerStatus == null || offerStatus.isBlank()
        || !offerStatus.equals("donated")
        && !offerStatus.equals("given")
    ) {
      throw new WrongBodyDataException("idMember or offer status are not valid.");
    }

    if (offerStatus.equals("donated")) {
      return this.jsonUtil
          .filterPublicJsonViewAsList(itemUCC.getMemberItemsByOfferStatus(idMember, offerStatus));
    }
    List<ItemDTO> itemDTOList = this.itemUCC.getMemberReceivedItems(idMember);
    return this.jsonUtil.filterPublicJsonViewAsList(itemDTOList);
  }

  /**
   * Asks UCC to get one offer identified by its id.
   *
   * @param idItem the item's id
   * @return the offer if it exists, otherwise throws a web application exception
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public ItemDTO getItem(@PathParam("id") int idItem) {
    if (idItem < 1) {
      throw new WrongBodyDataException("Item's id is lower than 1");
    }
    ItemDTO itemDTO = itemUCC.getOneItem(null, idItem);
    this.offerUCC.getLastTwoOffersOf(itemDTO);
    return this.jsonUtil.filterPublicJsonView(itemDTO);
  }

  /**
   * This method get items that have been assigned to this member identified by its id.
   *
   * @param idMember the member's id
   * @return the list of assigned items of the member
   */
  @GET
  @Path("assigned_items/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<ItemDTO> getAssignedItems(@PathParam("id") int idMember) {
    if (idMember < 1) {
      throw new WrongBodyDataException("idMember < 1 for get assigned items");
    }
    List<ItemDTO> itemDTOList = this.itemUCC.getAssignedItems(idMember);
    return this.jsonUtil.filterPublicJsonViewAsList(itemDTOList);
  }

  /**
   * This method get items that have been given by this member identified by its id.
   *
   * @param idMember the member's id
   * @return the list of given items of the member
   */
  @GET
  @Path("given_items/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<ItemDTO> getGivenItems(@PathParam("id") int idMember) {
    if (idMember < 1) {
      throw new WrongBodyDataException("idMember < 1 for get assigned items");
    }
    List<ItemDTO> itemDTOList = this.itemUCC.getGivenItems(idMember);
    return this.jsonUtil.filterPublicJsonViewAsList(itemDTOList);
  }

  /**
   * Count the number of items with a specific offer status for the member with the idMember.
   *
   * @param idMember    the member's id
   * @param offerStatus the offer status can only be "donated" or "given"
   * @return the number of items that match offer status
   * @throws WrongBodyDataException  if the offer status is invalid
   */
  @GET
  @Path("count/{idMember}/{offer_status}")
  @Consumes(MediaType.TEXT_PLAIN)
  @AuthorizeAdmin
  public int countNumberOfItemsByOfferStatus(@PathParam("idMember") int idMember,
      @PathParam("offer_status") String offerStatus) {
    if (idMember < 1
        || offerStatus == null || offerStatus.isBlank()
        || !offerStatus.equals("donated") && !offerStatus.equals("given")
    ) {
      throw new WrongBodyDataException("Wrong offerStatus is invalid.");
    }
    return this.itemUCC.countNumberOfItemsByOfferStatus(idMember, offerStatus);
  }

  /**
   * Count the number of items that have been received or not by the member. If received is true
   * that means the item has been received by the member. If received is false that means the member
   * marked his interest in the item but the member who offers the item marked the member has never
   * received the item.
   *
   * @param idMember the member's id
   * @param received true if the item has been received by the member false if the member had marked
   *                 its interest but never take the item.
   * @return the number of the number of items received or not received
   * @throws WrongBodyDataException  if the id member is lower than 1
   */
  @GET
  @Path("count_assigned_items/{idMember}/{received}")
  @AuthorizeAdmin
  public int countNumberOfReceivedOrNotReceivedItems(@PathParam("idMember") int idMember,
      @PathParam("received") boolean received) {
    if (idMember < 1) {
      throw new WrongBodyDataException("idMember is lower than 1");
    }

    return this.itemUCC.countNumberOfReceivedOrNotReceivedItems(idMember, received);
  }

  /**
   * Count the number of interested member for the last offer of the item identified by its id.
   *
   * @param idItem the item's id
   * @return the number of the interested member of the last item's offer
   * @throws WrongBodyDataException  if the idItem is lower than 1
   * @throws ObjectNotFoundException if the item doesn't exist in the database
   */
  @GET
  @Path("count_interested_members/{idItem}")
  @AuthorizeMember
  public int countNumberOfInterestedMember(@PathParam("idItem") int idItem) {
    if (idItem < 1) {
      throw new WrongBodyDataException("The idItem is lower than 1");
    }
    return this.offerUCC.getNumberOfInterestedMemberOf(idItem);
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
  public int addItem(ItemDTO itemDTO) {
    if (itemDTO == null
        || itemDTO.getItemDescription() == null || itemDTO.getItemDescription().isBlank()
        || itemDTO.getItemType() == null || itemDTO.getItemType().getItemType() == null
        || itemDTO.getItemType().getItemType().isBlank() || itemDTO.getMember() == null
        || itemDTO.getMember().getId() < 1 || itemDTO.getTitle() == null || itemDTO.getTitle()
        .isBlank() || itemDTO.getLastOffer() == null
        || itemDTO.getOfferList().get(0) == null
    ) {
      throw new WebApplicationException("Wrong item body", Status.BAD_REQUEST);
    }
    return this.itemUCC.addItem(itemDTO);
  }

  /////////////////////////////////////////////////////////
  ////////////////////////PUT//////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Mark the item identified by its id as given.
   *
   * @param itemDTO the item to update
   */
  @PUT
  @Path("given")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void markItemAsGiven(ItemDTO itemDTO) {
    if (itemDTO == null
        || itemDTO.getId() < 1) {
      throw new WrongBodyDataException("Missing information to mark item as given");
    }
    this.itemUCC.markItemAsGiven(itemDTO);
  }

  /**
   * Mark the item identified by its id as donated and update recipient to not received.
   *
   * @param itemDTO the item to update
   */
  @PUT
  @Path("not_given")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void markItemAsNotGiven(ItemDTO itemDTO) {
    this.itemUCC.markItemAsNotGiven(itemDTO);
  }

  /////////////////////////////////////////////////////////
  ///////////////////////DELETE////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Asks the UCC to cancel the item's offer.
   *
   * @param idItem the item's id
   * @return the canceled item
   */
  @DELETE
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public ItemDTO cancelOffer(@PathParam("id") int idItem) {
    if (itemUCC.getOneItem(null, idItem) == null) {
      throw new ObjectNotFoundException("Item not found");
    }
    ItemDTO itemDTO = itemUCC.cancelItem(idItem);
    if (itemDTO == null) {
      throw new FatalException("Canceling item returned null");
    }
    return this.jsonUtil.filterPublicJsonView(itemDTO);
  }

  /////////////////////////////////////////////////////////
  ///////////////////////PUT///////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Modify the item.
   *
   * @param itemDTO the new item
   */
  @PUT
  @Path("modify")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void modifyItem(ItemDTO itemDTO) {
    if (itemDTO == null
        || itemDTO.getId() < 1
        || itemDTO.getItemDescription() == null || itemDTO.getItemDescription().isBlank()
        || itemDTO.getLastOffer() == null || itemDTO.getLastOffer().getTimeSlot() == null
        || itemDTO.getLastOffer().getTimeSlot().isBlank()) {
      throw new WrongBodyDataException("itemDTO not complete.");
    }
    itemUCC.modifyItem(itemDTO);
  }
}
