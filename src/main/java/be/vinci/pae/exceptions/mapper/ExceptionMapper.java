package be.vinci.pae.exceptions.mapper;

import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ForbiddenException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.TokenDecodingException;
import be.vinci.pae.exceptions.webapplication.UnauthorizedException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.logs.LoggerHandler;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<Throwable> {

  private final Logger logger = LoggerHandler.getLogger();

  @Override
  public Response toResponse(Throwable throwable) {
    String logMessage = throwable.getMessage() + "\n";
    logMessage += this.stackTraceToString(throwable.getStackTrace());
    this.logger.log(Level.SEVERE, logMessage);
    if (throwable instanceof ConflictException) {
      return ((ConflictException) throwable).getResponse();
    }
    if (throwable instanceof ForbiddenException) {
      return ((ForbiddenException) throwable).getResponse();
    }
    if (throwable instanceof ObjectNotFoundException) {
      return ((ObjectNotFoundException) throwable).getResponse();
    }
    if (throwable instanceof TokenDecodingException) {
      return ((TokenDecodingException) throwable).getResponse();
    }
    if (throwable instanceof UnauthorizedException) {
      return ((UnauthorizedException) throwable).getResponse();
    }
    if (throwable instanceof WrongBodyDataException) {
      return ((WrongBodyDataException) throwable).getResponse();
    }
    return Response.status(Status.INTERNAL_SERVER_ERROR)
        .entity(throwable.getMessage())
        .build();
  }

  private String stackTraceToString(StackTraceElement[] stackTraceElements) {
    return Arrays.toString(stackTraceElements)
        .replace(",", "\n")
        .replace("[", "")
        .replace("]", "");
  }
}
