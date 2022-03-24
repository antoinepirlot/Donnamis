package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class ObjectNotFoundException extends WebApplicationException {

  public ObjectNotFoundException() {
    super(Response.status(Status.NOT_FOUND)
        .build());
  }

  public ObjectNotFoundException(String message) {
    super(Response.status(Status.NOT_FOUND)
        .entity(message)
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  public ObjectNotFoundException(Throwable throwable) {
    super(Response.status(Status.NOT_FOUND)
        .entity(throwable.getMessage())
        .type(MediaType.TEXT_PLAIN)
        .build());
  }
}
