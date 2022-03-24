package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class WrongBodyDataException extends WebApplicationException {

  public WrongBodyDataException() {
    super(Response.status(Status.BAD_REQUEST)
        .build());
  }

  public WrongBodyDataException(String message) {
    super(Response.status(Status.BAD_REQUEST)
        .entity(message)
        .type(MediaType.TEXT_PLAIN_TYPE)
        .build());
  }

  public WrongBodyDataException(Throwable throwable) {
    super(Response.status(Status.BAD_REQUEST)
        .entity(throwable.getMessage())
        .type(MediaType.TEXT_PLAIN_TYPE)
        .build());
  }
}
