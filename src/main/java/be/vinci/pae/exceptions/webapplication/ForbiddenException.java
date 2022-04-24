package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class ForbiddenException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with FORBIDDEN status and generic entity.
   */
  public ForbiddenException() {
    super(Status.FORBIDDEN);
  }

  /**
   * Call super (WebApplicationException) with FORBIDDEN status with a message.
   *
   * @param message the message to add in entity
   */
  public ForbiddenException(String message) {
    super(message, Status.FORBIDDEN);
  }

  /**
   * Call super (WebApplicationException) with FORBIDDEN status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public ForbiddenException(Throwable throwable) {
    super(throwable, Status.FORBIDDEN);
  }
}
