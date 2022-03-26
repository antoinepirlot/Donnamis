package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Arrays;

public class UnauthorizedException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status and generic entity.
   */
  public UnauthorizedException() {
    super(Response.status(Status.UNAUTHORIZED)
        .entity("Unauthorized")
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status with a message.
   *
   * @param message the message to add in entity
   */
  public UnauthorizedException(String message) {
    super(Response.status(Status.UNAUTHORIZED)
        .entity(message)
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  /**
   * Call super (WebApplicationException) with UNAUTHORIZED status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public UnauthorizedException(Throwable throwable) {
    super(Response.status(Status.UNAUTHORIZED)
        .entity(throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()))
        .type(MediaType.TEXT_PLAIN)
        .build());
  }
}
