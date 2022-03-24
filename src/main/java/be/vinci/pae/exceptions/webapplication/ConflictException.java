package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;

public class ConflictException extends WebApplicationException{

  public ConflictException() {
    super(Response.status(Response.Status.CONFLICT)
        .entity("this resource already exists")
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  public ConflictException(String message) {
    super(Response.status(Response.Status.CONFLICT)
        .entity(message)
        .type(MediaType.TEXT_PLAIN)
        .build());
  }

  public ConflictException(Throwable throwable) {
    super(Response.status(Response.Status.CONFLICT)
        .entity(throwable.getMessage() + "\n" + Arrays.toString(throwable.getStackTrace()))
        .type(MediaType.TEXT_PLAIN)
        .build());
  }
}
