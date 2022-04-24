package be.vinci.pae.exceptions.webapplication;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

public class WrongBodyDataException extends WebApplicationException {

  /**
   * Call super (WebApplicationException) with BAD REQUEST status and generic entity.
   */
  public WrongBodyDataException() {
    super(Status.BAD_REQUEST);
  }

  /**
   * Call super (WebApplicationException) with BAD REQUEST status and a message.
   *
   * @param message the message to add in entity
   */
  public WrongBodyDataException(String message) {
    super(message, Status.BAD_REQUEST);
  }

  /**
   * Call super (WebApplicationException) with BAD REQUEST status with throwable information.
   *
   * @param throwable the throwable that contains information about the error
   */
  public WrongBodyDataException(Throwable throwable) {
    super(throwable, Status.BAD_REQUEST);
  }
}
