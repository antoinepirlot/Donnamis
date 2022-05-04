package be.vinci.pae.ihm.recipient;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.recipient.interfaces.RecipientUCC;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ForbiddenException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
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
  @Inject
  private MemberUCC memberUCC;

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

    //Get the member by his username
    MemberDTO memberDTO = memberUCC.getOneMember(recipientDTO.getMember());
    if (memberDTO == null) {
      throw new ObjectNotFoundException("Member doesn't exist");
    }

    //Verify the correct state (confirmed) of the member
    if (!memberDTO.getActualState().equals("confirmed")) {
      throw new ConflictException("Member need to be confirmed");
    }

    //Verify if the recipients already exist in the DB
    if (this.recipientUCC.exists(recipientDTO)) {
      throw new ForbiddenException("This recipient already exists for this offer.");
    }

    //Create the new recipient
    if (!this.recipientUCC.chooseRecipient(recipientDTO)) {
      throw new FatalException("choose recipient returned false.");
    }
  }

  @PUT
  @Path("unavailable")
  @Consumes(MediaType.APPLICATION_JSON)
  public void setRecipientUnavailable(RecipientDTO recipientDTO) {

    //Verify the content of the body of the request
    if (recipientDTO == null || recipientDTO.getId() < 1) {
      throw new WrongBodyDataException("Recipient is null or wrong id");
    }

    //Verify if the member exist
    if (memberUCC.getOneMember(recipientDTO.getMember()) == null) {
      throw new ObjectNotFoundException("Member doesn't exist");
    }

    //Change the state of the recipient
    if (recipientUCC.setRecipientUnavailable(recipientDTO)) {
      throw new FatalException("An unexpected error happened while set recipient unavailable");
    }

  }


}
