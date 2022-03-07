package be.vinci.pae.ihm;

import be.vinci.pae.biz.ItemDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Path("items")
public class ItemResource<ItemUCC> {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private ItemUCC itemUCC;

  /*
  Method that get all the latest items offered
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ItemDTO> getLatestItems() {
    return null;
    //return itemUCC.getLatestItems();
  }


}
