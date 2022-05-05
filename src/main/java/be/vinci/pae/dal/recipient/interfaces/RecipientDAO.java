package be.vinci.pae.dal.recipient.interfaces;

import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;

public interface RecipientDAO {

  /**
   * Add te recipient into the database.
   *
   * @param recipientDTO the recipient to add
   * @return true if it's added otherwise false
   */
  boolean chooseRecipient(RecipientDTO recipientDTO);

  /**
   * Checks if the recipient already exists for the current offer.
   *
   * @param recipientDTO the recipient to check
   * @return true if it exists otherwise false
   */
  boolean exists(RecipientDTO recipientDTO);

  /**
   * Set the field received "not received" for the recipient if the field received is actually
   * "waiting".
   *
   * @param recipientDTO the recipient to change
   * @return true if success otherwise false
   */
  boolean setRecipientUnavailable(RecipientDTO recipientDTO);
}
