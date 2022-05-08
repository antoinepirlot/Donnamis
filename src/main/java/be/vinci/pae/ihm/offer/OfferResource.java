package be.vinci.pae.ihm.offer;

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
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Singleton
@Path("offers")
public class OfferResource {

  private final Json<OfferDTO> jsonUtil = new Json<>(OfferDTO.class);
  @Inject
  private OfferUCC offerUCC;

  /////////////////////////////////////////////////////////
  ///////////////////////GET///////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Method that get all offers.
   *
   * @return the list of all offers if there's at least one offer, otherwise null
   */
  @GET
  @Path("all_offers")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<OfferDTO> getAllOffers() {
    return this.getAllOffers(null);
  }

  /**
   * Method that get all offers with specified offer status.
   *
   * @return the list of all offers if there's at least one offer, otherwise null
   */
  @GET
  @Path("all_offers/{offer_status}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<OfferDTO> getAllOffers(@PathParam("offer_status") String offerStatus) {
    List<OfferDTO> listOfferDTO = offerUCC.getAllOffers(offerStatus);
    return this.jsonUtil.filterPublicJsonViewAsList(listOfferDTO);
  }

  /**
   * Method that get one offer identified by its id.
   *
   * @return the offer
   * @throws ObjectNotFoundException if the offer does not exist
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public OfferDTO getOneOffer(@PathParam("id") int id) {
    OfferDTO offerDTO = offerUCC.getOneOffer(id);
    return this.jsonUtil.filterPublicJsonView(offerDTO);
  }

  /////////////////////////////////////////////////////////
  ///////////////////////POST//////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Asks UCC to add an offer into the database.
   *
   * @param offerDTO the offer to add into the database
   */
  @POST
  @Path("add_offer")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void addOffer(OfferDTO offerDTO) {
    // Get and check credentials
    if (offerDTO == null || offerDTO.getIdItem() < 1) {
      String message;
      if (offerDTO == null) {
        message = "offerDTO is null.";
      } else {
        message = "item id = " + offerDTO.getIdItem() + " and is not greater than 0.";
      }
      throw new WrongBodyDataException(message);
    }
    //Try to create an offer
    offerUCC.createOffer(offerDTO);
  }
}
