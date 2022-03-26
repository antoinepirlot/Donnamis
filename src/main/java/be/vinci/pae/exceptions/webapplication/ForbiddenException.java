package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Arrays;

public class ForbiddenException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with FORBIDDEN status and generic entity.
   */
  public ForbiddenException() {
    super(Response.status(Status.FORBIDDEN)
        .entity("Forbidden")
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  /**
   * Call super (WebApplicationException) with FORBIDDEN status with a message.
   *
   * @param message the message to add in entity
   */
  public ForbiddenException(String message) {
    super(Response.status(Status.FORBIDDEN)
        .entity(message)
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  /**
   * Call super (WebApplicationException) with FORBIDDEN status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public ForbiddenException(Throwable throwable) {
    super(Response.status(Status.FORBIDDEN)
        .entity(throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()))
        .type(MediaType.TEXT_PLAIN)
        .build());
  }
}
