package be.vinci.pae.biz.recipient.interfaces;

public interface RecipientUCC {

  /**
   * add the recipient into the database.
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
}
