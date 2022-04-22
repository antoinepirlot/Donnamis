package be.vinci.pae.biz.recipient.interfaces;

import java.sql.SQLException;

public interface RecipientUCC {

  /**
   * add the recipient into the database
   *
   * @param recipientDTO the recipient to add
   * @return true if it's added otherwise false
   * @throws SQLException if an error occurs while adding it
   */
  boolean chooseRecipient(RecipientDTO recipientDTO) throws SQLException;
}
