package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class UnauthorizedException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status and generic entity.
   */
  public UnauthorizedException() {
    super(Status.UNAUTHORIZED);
  }

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status with a message.
   *
   * @param message the message to add in entity
   */
  public UnauthorizedException(String message) {
    super(message, Status.UNAUTHORIZED);
  }

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public UnauthorizedException(Throwable throwable) {
    super(throwable, Status.UNAUTHORIZED);
  }
}
