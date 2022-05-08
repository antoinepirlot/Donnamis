package be.vinci.pae.ihm.recipient;

import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.recipient.interfaces.RecipientUCC;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeAdmin;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("recipients")
@AuthorizeMember
public class RecipientResource {

  @Inject
  private RecipientUCC recipientUCC;

  /**
   * Add a recipient into the database.
   *
   * @param recipientDTO the recipient to add
   */
  @POST
  @Path("")
  @Consumes(MediaType.APPLICATION_JSON)
  public void chooseRecipient(RecipientDTO recipientDTO) {

    //Verify the content of the body of the request
    if (recipientDTO == null
        || recipientDTO.getItem() == null || recipientDTO.getItem().getId() < 1
        || recipientDTO.getMember() == null || recipientDTO.getMember().getUsername() == null
        || recipientDTO.getMember().getUsername().isBlank()
    ) {
      throw new WrongBodyDataException("RecipientDTO is incomplete");
    }
    //Create the new recipient
    this.recipientUCC.chooseRecipient(recipientDTO);
  }

  /**
   * Set the recipient received attribute to not received.
   *
   * @param recipientDTO the recipient to modify
   */
  @PUT
  @Path("unavailable")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public void setRecipientUnavailable(RecipientDTO recipientDTO) {
    //Verify the content of the body of the request
    if (recipientDTO == null || recipientDTO.getMember().getId() < 1) {
      throw new WrongBodyDataException("Recipient is null or wrong id");
    }
    //Change the state of the recipient
    recipientUCC.setRecipientUnavailable(recipientDTO);
  }
}
