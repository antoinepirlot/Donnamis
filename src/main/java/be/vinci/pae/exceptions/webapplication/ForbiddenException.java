package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Arrays;

public class ForbiddenException  extends WebApplicationException {
  public ForbiddenException() {
    super(Response.status(Status.FORBIDDEN)
        .entity("Forbidden")
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  public ForbiddenException(String message) {
    super(Response.status(Status.FORBIDDEN)
        .entity(message)
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  public ForbiddenException(Throwable throwable) {
    super(Response.status(Status.FORBIDDEN)
        .entity(throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()))
        .type(MediaType.TEXT_PLAIN)
        .build());
  }
}
