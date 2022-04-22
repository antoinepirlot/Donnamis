package be.vinci.pae.ihm.recipient;

import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.recipient.interfaces.RecipientUCC;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.rmi.UnexpectedException;
import java.sql.SQLException;

@Singleton
@Path("recipients")
public class RecipientResource {

  @Inject
  private RecipientUCC recipientUCC;

  /**
   * Add a recipient into the database
   *
   * @param recipientDTO the recipient to add
   * @throws SQLException        if an error occurs while adding it
   * @throws UnexpectedException if chooseRecipient returned false
   */
  @POST
  @Path("")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public void chooseRecipient(RecipientDTO recipientDTO) throws SQLException, UnexpectedException {
    if (recipientDTO == null
        || recipientDTO.getItem() == null || recipientDTO.getItem().getId() < 1
        || recipientDTO.getMember() == null || recipientDTO.getMember().getUsername() == null
        || recipientDTO.getMember().getUsername().isBlank()
    ) {
      throw new WrongBodyDataException("RecipientDTO is incomplete");
    }
    if (!this.recipientUCC.chooseRecipient(recipientDTO)) {
      throw new UnexpectedException("choose recipient returned false.");
    }
  }

}
