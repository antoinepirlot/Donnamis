package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

public class TokenDecodingException extends WebApplicationException {

  public TokenDecodingException() {
    super(Response.status(Status.UNAUTHORIZED)
        .build());
  }

  public TokenDecodingException(String message) {
    super(Response.status(Status.UNAUTHORIZED)
        .entity(message)
        .type(MediaType.TEXT_PLAIN_TYPE)
        .build());
  }

  public TokenDecodingException(Throwable throwable) {
    super(Response.status(Status.UNAUTHORIZED)
        .entity(throwable.getMessage())
        .type(MediaType.TEXT_PLAIN_TYPE)
        .build());
  }
}
