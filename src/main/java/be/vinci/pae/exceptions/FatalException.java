package be.vinci.pae.exceptions;

public class FatalException extends RuntimeException {

  /**
   * Call super.
   */
  public FatalException() {
    super();
  }

  /**
   * Call super with a message.
   *
   * @param message the message of the exception
   */
  public FatalException(String message) {
    super(message);
  }

  /**
   * Call super with the throwable.
   *
   * @param throwable the throwable that contains information
   */
  public FatalException(Throwable throwable) {
    super(throwable);
  }
}
