package be.vinci.pae.biz.recipient.objects;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.recipient.interfaces.RecipientUCC;
import be.vinci.pae.dal.recipient.interfaces.RecipientDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ForbiddenException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class RecipientUCCImpl implements RecipientUCC {

  @Inject
  private DALServices dalServices;
  @Inject
  private MemberUCC memberUCC;
  @Inject
  private RecipientDAO recipientDAO;

  @Override
  public void chooseRecipient(RecipientDTO recipientDTO) {
    //Get the member by his username
    MemberDTO memberDTO = memberUCC.getOneMember(recipientDTO.getMember());
    if (memberDTO == null) {
      throw new ObjectNotFoundException("Member doesn't exist");
    }
    //Verify the correct state (confirmed) of the member
    if (!memberDTO.getActualState().equals("confirmed")) {
      throw new ForbiddenException("Member need to be confirmed");
    }
    //Verify if the recipients already exist in the DB
    if (this.exists(recipientDTO)) {
      throw new ConflictException("This recipient already exists for this offer.");
    }
    try {
      boolean done;
      this.dalServices.start();
      done = this.recipientDAO.chooseRecipient(recipientDTO);
      this.dalServices.commit();
      if (!done) {
        throw new FatalException("choose recipient returned false.");
      }
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean exists(RecipientDTO recipientDTO) {
    try {
      boolean exists;
      this.dalServices.start();
      exists = this.recipientDAO.exists(recipientDTO);
      this.dalServices.commit();
      return exists;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public void setRecipientUnavailable(RecipientDTO recipientDTO) {
    //Verify if the member exist
    if (!memberUCC.memberExist(recipientDTO.getMember(), -1)) {
      throw new ObjectNotFoundException("Member doesn't exist");
    }
    try {
      this.dalServices.start();
      boolean setCorrectly = this.recipientDAO.setRecipientUnavailable(recipientDTO);
      this.dalServices.commit();
      if (!setCorrectly) {
        throw new FatalException("An unexpected error happened while set recipient unavailable");
      }
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw new FatalException(e);
    }
  }
}
