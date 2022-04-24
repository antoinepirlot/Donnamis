package be.vinci.pae.biz.recipient.objects;

import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.recipient.interfaces.RecipientUCC;
import be.vinci.pae.dal.recipient.interfaces.RecipientDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class RecipientUCCImpl implements RecipientUCC {

  @Inject
  private DALServices dalServices;
  @Inject
  private RecipientDAO recipientDAO;

  @Override
  public boolean chooseRecipient(RecipientDTO recipientDTO) throws SQLException {
    try {
      boolean done;
      this.dalServices.start();
      done = this.recipientDAO.chooseRecipient(recipientDTO);
      this.dalServices.commit();
      return done;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw e;
    }
  }

  @Override
  public boolean exists(RecipientDTO recipientDTO) throws SQLException {
    try {
      boolean exists;
      this.dalServices.start();
      exists = this.recipientDAO.exists(recipientDTO);
      this.dalServices.commit();
      return exists;
    } catch (SQLException e) {
      this.dalServices.rollback();
      throw e;
    }
  }
}
