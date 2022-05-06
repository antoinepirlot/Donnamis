package be.vinci.pae.ihm.item;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ForbiddenException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeAdmin;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.filter.utils.Json;
import be.vinci.pae.utils.Config;
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
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import org.apache.maven.surefire.shared.io.FileUtils;

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
    try {
      List<ItemDTO> itemDTOList = this.getAllItemsByOfferStatusOrIdMember(null, -1);
      for (ItemDTO itemDTO : itemDTOList) {
        itemDTO.setPhoto(transformImageToBase64(itemDTO));
      }
      return itemDTOList;
    } catch (SQLException | IOException e) {
      throw new FatalException("Can't get all items");
    }
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
    try {
      List<ItemDTO> itemDTOList = this.getAllItemsByOfferStatusOrIdMember(offerStatus, -1);
      for (ItemDTO itemDTO : itemDTOList) {
        itemDTO.setPhoto(transformImageToBase64(itemDTO));
      }
      return itemDTOList;
    } catch (SQLException | IOException e) {
      throw new FatalException("Can't get all items by offer status: " + offerStatus);
    }
  }

  @GET
  @Path("all_items/public")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getAllPublicItems() {
    try {
      List<ItemDTO> itemDTOList = this.itemUCC.getAllPublicItems();
      for (ItemDTO itemDTO : itemDTOList) {
        itemDTO.setPhoto(transformImageToBase64(itemDTO));
      }
      return itemDTOList;
    } catch (IOException e) {
      throw new FatalException("Can't get all public items");
    }
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
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("This member doesn't exists.");
    }
    try {
      List<ItemDTO> itemDTOList = this.getAllItemsByOfferStatusOrIdMember(null, idMember);
      for (ItemDTO itemDTO : itemDTOList) {
        itemDTO.setPhoto(transformImageToBase64(itemDTO));
      }
      return itemDTOList;
    } catch (SQLException | IOException e) {
      throw new FatalException("Can't get all items by member id: " + idMember);
    }
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
    if (itemDTOList == null) {
      return null;
    }

    for (ItemDTO itemDTO : itemDTOList) {
      try {
        itemDTO.setPhoto(transformImageToBase64(itemDTO));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return this.jsonUtil.filterPublicJsonViewAsList(itemDTOList);
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
  public ItemDTO getItem(@PathParam("id") int id) {
    ItemDTO itemDTO = itemUCC.getOneItem(id);
    if (itemDTO == null) {
      throw new ObjectNotFoundException("No item matching id: " + id);
    }
    try {
      itemDTO.setPhoto(transformImageToBase64(itemDTO));
    } catch (IOException e) {
      e.printStackTrace();
    }
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
    if (idMember < 0) {
      throw new WrongBodyDataException("idMember < 0 for get assigned items");
    }
    List<ItemDTO> itemDTOList = this.itemUCC.getAssignedItems(idMember);

    if (itemDTOList == null) {
      throw new ObjectNotFoundException("No assigned items");
    }
    for (ItemDTO itemDTO : itemDTOList) {
      try {
        itemDTO.setPhoto(transformImageToBase64(itemDTO));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return this.jsonUtil.filterPublicJsonViewAsList(itemDTOList);
  }

  /**
   * Count the number of items with a specific offer status for the member with the idMember.
   *
   * @param idMember    the member's id
   * @param offerStatus the offer status can only be "donated" or "given"
   * @return the number of items that match offer status
   * @throws WrongBodyDataException  if the offer status is invalid
   * @throws ObjectNotFoundException if the member doesn't exist
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
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("The member " + idMember + " doesn't exist.");
    }
    int count = this.itemUCC.countNumberOfItemsByOfferStatus(idMember, offerStatus);
    if (count == -1) {
      throw new FatalException("count number of items returned -1.");
    }
    return count;
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
   * @throws ObjectNotFoundException if the member doesn't exist in the database
   */
  @GET
  @Path("count_assigned_items/{idMember}/{received}")
  @AuthorizeAdmin
  public int countNumberOfReceivedOrNotReceivedItems(@PathParam("idMember") int idMember,
      @PathParam("received") boolean received) {
    if (idMember < 1) {
      throw new WrongBodyDataException("idMember is lower than 1");
    }
    if (!this.memberUCC.memberExist(null, idMember)) {
      throw new ObjectNotFoundException("The member " + idMember + " doesn't exist.");
    }
    int count = this.itemUCC.countNumberOfReceivedOrNotReceivedItems(idMember, received);
    if (count == -1) {
      throw new FatalException("Count number returned -1");
    }
    return count;
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
        || itemDTO.getOfferList().get(0) == null) {
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
    return idItem;
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
    this.checkMarkItem(itemDTO);

    if (!this.itemUCC.markItemAsGiven(itemDTO)) {
      throw new FatalException("marking item " + itemDTO.getId() + " as given failed.");
    }
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
    this.checkMarkItem(itemDTO);
    if (!this.itemUCC.markItemAsNotGiven(itemDTO)) {
      throw new FatalException("marking item " + itemDTO.getId() + " as not given failed.");
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
  public ItemDTO cancelOffer(@PathParam("id") int id) {
    if (itemUCC.getOneItem(id) == null) {
      throw new ObjectNotFoundException("Item not found");
    }
    ItemDTO itemDTO = itemUCC.cancelItem(id);
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
    if (itemDTO.getVersion() != itemUCC.getOneItem(itemDTO.getId()).getVersion()) {
      throw new FatalException("Error with version");
    }
    if (this.itemUCC.getOneItem(itemDTO.getId()) == null) {
      throw new ObjectNotFoundException("No item with the id : " + itemDTO.getId());
    }
    if (!itemUCC.modifyItem(itemDTO)) {
      throw new FatalException("Unexpected error");
    }
  }

  /////////////////////////////////////////////////////////
  ///////////////////////UTILS/////////////////////////////
  /////////////////////////////////////////////////////////

  private List<ItemDTO> getAllItemsByOfferStatusOrIdMember(String offerStatus, int idMember)
      throws SQLException {
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
      this.offerUCC.getLastTwoOffersOf(itemDTO);
    }
    return listItemDTO;
  }

  private String transformImageToBase64(ItemDTO itemDTO) throws IOException {
    if (itemDTO == null
        || itemDTO.getPhoto() == null || itemDTO.getPhoto().isBlank()) {
      return null;
    }
    String photoSignature = itemDTO.getPhoto();
    String path = Config.getPhotoPath();
    String photoPath = path + "\\" + photoSignature;
    byte[] fileContent;
    try {
      fileContent = FileUtils.readFileToByteArray(new File(photoPath));
    } catch (IOException e) {
      throw new FatalException(e);
    }
    return Base64.getEncoder().encodeToString(fileContent);
  }

  /**
   * Check itemDTO for mark item as {offerStatus}.
   *
   * @param itemDTO the item to check
   */
  private void checkMarkItem(ItemDTO itemDTO) {
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
      throw new WrongBodyDataException(
          "The member's id and item's member's id are not associated");
    }
    if (existingItem.getOfferStatus().equals("given")) {
      throw new ForbiddenException("The item " + itemDTO.getId() + " is already given.");
    }

    if (itemDTO.getVersion() != itemUCC.getOneItem(itemDTO.getId()).getVersion()) {
      throw new FatalException("Error with version");
    }
  }
}
