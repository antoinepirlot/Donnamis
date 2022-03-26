package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;

public class ConflictException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with CONFLICT status and generic entity.
   */
  public ConflictException() {
    super(Response.status(Response.Status.CONFLICT)
        .entity("this resource already exists")
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  /**
   * Call super (WebApplicationException) with CONFLICT status with a message.
   *
   * @param message the message to add in entity
   */
  public ConflictException(String message) {
    super(Response.status(Response.Status.CONFLICT)
        .entity(message)
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  /**
   * Call super (WebApplicationException) with CONFLICT status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public ConflictException(Throwable throwable) {
    super(Response.status(Response.Status.CONFLICT)
        .entity(throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()))
        .type(MediaType.TEXT_PLAIN)
        .build());
  }
}
