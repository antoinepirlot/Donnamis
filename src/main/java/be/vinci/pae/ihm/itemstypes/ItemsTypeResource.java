package be.vinci.pae.ihm.itemstypes;

import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeUCC;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Path("items_types")
public class ItemsTypeResource {

  @Inject
  private ItemsTypeUCC itemsTypeUCC;

  /////////////////////////////////////////////////////////
  ///////////////////////GET///////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Ask itemsTypeUCC to get all items types. If there's no items types it throws an
   * ObjectNotFoundException.
   *
   * @return a lit of items types
   */
  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<ItemsTypeDTO> getAll() {
    List<ItemsTypeDTO> itemsTypeDTOList = this.itemsTypeUCC.getAll();
    if (itemsTypeDTOList == null) {
      String message = "No items type found.";
      throw new ObjectNotFoundException(message);
    }
    return itemsTypeDTOList;
  }

  /////////////////////////////////////////////////////////
  ///////////////////////POST//////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Add a new Items type into the database.
   *
   * @param itemsTypeDTO the new items type
   */
  @POST
  @Path("")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void addItemsType(ItemsTypeDTO itemsTypeDTO) {
    if (itemsTypeDTO == null
        || itemsTypeDTO.getItemType() == null || itemsTypeDTO.getItemType().isBlank()
    ) {
      throw new WrongBodyDataException("ItemsType is incomplete.");
    }
    if (this.itemsTypeUCC.exists(itemsTypeDTO)) {
      String message = "The items type " + itemsTypeDTO.getItemType() + " already exist";
      throw new ConflictException(message);
    }
    if (!this.itemsTypeUCC.addItemsType(itemsTypeDTO)) {
      throw new FatalException("Unexpected exception while adding new itemsType");
    }
  }
}
