package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class ObjectNotFoundException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with NOT FOUND status and generic entity.
   */
  public ObjectNotFoundException() {
    super(Response.status(Status.NOT_FOUND)
        .entity("Resource not found")
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  /**
   * Call super (WebApplicationException) with NOT FOUND status with a message.
   *
   * @param message the message to add in entity
   */
  public ObjectNotFoundException(String message) {
    super(Response.status(Status.NOT_FOUND)
        .entity(message)
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  /**
   * Call super (WebApplicationException) with NOT FOUND status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public ObjectNotFoundException(Throwable throwable) {
    super(Response.status(Status.NOT_FOUND)
        .entity(throwable.getMessage())
        .type(MediaType.TEXT_PLAIN)
        .build());
  }
}
