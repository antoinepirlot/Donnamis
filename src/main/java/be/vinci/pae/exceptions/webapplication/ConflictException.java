package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class ConflictException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with CONFLICT status and generic entity.
   */
  public ConflictException() {
    super(Status.CONFLICT);
  }

  /**
   * Call super (WebApplicationException) with CONFLICT status with a message.
   *
   * @param message the message to add in entity
   */
  public ConflictException(String message) {
    super(message, Status.CONFLICT);
  }

  /**
   * Call super (WebApplicationException) with CONFLICT status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public ConflictException(Throwable throwable) {
    super(throwable, Status.CONFLICT);
  }
}
