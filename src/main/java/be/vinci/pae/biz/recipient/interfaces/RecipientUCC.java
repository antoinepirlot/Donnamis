package be.vinci.pae.biz.recipient.interfaces;

public interface RecipientUCC {

  /**
   * add the recipient into the database.
   *
   * @param recipientDTO the recipient to add
   */
  void chooseRecipient(RecipientDTO recipientDTO);

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
   */
  void setRecipientUnavailable(RecipientDTO recipientDTO);
}
