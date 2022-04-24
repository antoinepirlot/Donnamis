package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class ObjectNotFoundException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with NOT FOUND status and generic entity.
   */
  public ObjectNotFoundException() {
    super(Status.NOT_FOUND);
  }

  /**
   * Call super (WebApplicationException) with NOT FOUND status with a message.
   *
   * @param message the message to add in entity
   */
  public ObjectNotFoundException(String message) {
    super(message, Status.NOT_FOUND);
  }

  /**
   * Call super (WebApplicationException) with NOT FOUND status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public ObjectNotFoundException(Throwable throwable) {
    super(throwable, Status.NOT_FOUND);
  }
}
