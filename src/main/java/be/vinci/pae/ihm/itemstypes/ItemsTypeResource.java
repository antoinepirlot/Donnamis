package be.vinci.pae.ihm.itemstypes;

import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeUCC;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.filter.utils.Json;
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
@AuthorizeMember
public class ItemsTypeResource {

  private final Json<ItemsTypeDTO> jsonUtil = new Json<>(ItemsTypeDTO.class);
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
  public List<ItemsTypeDTO> getAll() {
    return this.jsonUtil.filterPublicJsonViewAsList(this.itemsTypeUCC.getAll());
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
  public void addItemsType(ItemsTypeDTO itemsTypeDTO) {
    if (itemsTypeDTO == null
        || itemsTypeDTO.getItemType() == null || itemsTypeDTO.getItemType().isBlank()
    ) {
      throw new WrongBodyDataException("ItemsType is incomplete.");
    }
    this.itemsTypeUCC.addItemsType(itemsTypeDTO);
  }
}
