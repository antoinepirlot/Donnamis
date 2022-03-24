package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Arrays;

public class UnauthorizedException extends WebApplicationException {

  public UnauthorizedException() {
    super(Response.status(Status.UNAUTHORIZED)
        .build());
  }

  public UnauthorizedException(String message) {
    super(Response.status(Status.UNAUTHORIZED)
        .entity(message)
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  public UnauthorizedException(Throwable throwable) {
    super(Response.status(Status.UNAUTHORIZED)
        .entity(throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()))
        .type(MediaType.TEXT_PLAIN)
        .build());
  }
}
