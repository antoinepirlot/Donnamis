package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class TokenDecodingException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status and generic entity.
   */
  public TokenDecodingException() {
    super(Status.UNAUTHORIZED);
  }

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status with a message.
   *
   * @param message the message to add in entity
   */
  public TokenDecodingException(String message) {
    super(message, Status.UNAUTHORIZED);
  }

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public TokenDecodingException(Throwable throwable) {
    super(throwable, Status.UNAUTHORIZED);
  }
}
