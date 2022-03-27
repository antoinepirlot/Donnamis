package be.vinci.pae.ihm.itemstypes;

import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeUCC;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.utils.Json;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Path("items_types")
public class ItemsTypeResource {

  @Inject
  private ItemsTypeUCC itemsTypeUCC;
  private final Json<ItemDTO> jsonUtil = new Json<>(ItemDTO.class);

  @GET
  @Path("all")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<ItemsTypeDTO> getAll(){
    List<ItemsTypeDTO> itemsTypeDTOList = this.itemsTypeUCC.getAll();
    if (itemsTypeDTOList == null) {
      String message = "No items type found.";
      throw new ObjectNotFoundException(message);
    }
    return itemsTypeDTOList;
  }
}
