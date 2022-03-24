package be.vinci.pae.utils.exceptions;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.logging.Level;

public class ObjectNotFoundException extends Exception {

  public ObjectNotFoundException() {
    super("Object not found");
  }

  public ObjectNotFoundException(String message) {
    super(message);
  }

  public ObjectNotFoundException(String message, Throwable throwable) {
    super(message, throwable);
  }
  public ObjectNotFoundException(Throwable throwable) {
    super(throwable);
  }
}
