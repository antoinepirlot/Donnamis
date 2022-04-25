package be.vinci.pae.ihm.recipient;

import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.recipient.interfaces.RecipientUCC;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Singleton
@Path("recipients")
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
  @AuthorizeMember
  public void chooseRecipient(RecipientDTO recipientDTO) {
    if (recipientDTO == null
        || recipientDTO.getItem() == null || recipientDTO.getItem().getId() < 1
        || recipientDTO.getMember() == null || recipientDTO.getMember().getUsername() == null
        || recipientDTO.getMember().getUsername().isBlank()
    ) {
      throw new WrongBodyDataException("RecipientDTO is incomplete");
    }
    if (this.recipientUCC.exists(recipientDTO)) {
      throw new ConflictException("This recipient already exists for this offer.");
    }
    if (!this.recipientUCC.chooseRecipient(recipientDTO)) {
      throw new FatalException("choose recipient returned false.");
    }
  }

}
