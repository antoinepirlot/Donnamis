package be.vinci.pae.exceptions;

public class TransactionException extends Exception {

  /**
   * Call super (Exception).
   */
  public TransactionException() {
    super();
  }

  /**
   * Call super (Exception) with a message.
   *
   * @param message the message of the exception
   */
  public TransactionException(String message) {
    super(message);
  }

  /**
   * Call super (Exception) with the throwable.
   *
   * @param throwable the throwable that contains information
   */
  public TransactionException(Throwable throwable) {
    super(throwable);
  }
}
