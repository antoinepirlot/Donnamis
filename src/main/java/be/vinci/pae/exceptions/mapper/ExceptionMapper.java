package be.vinci.pae.exceptions.mapper;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<Throwable> {


  @Override
  public Response toResponse(Throwable throwable) {
    throwable.printStackTrace();
    Response response;
    if (throwable instanceof WebApplicationException) {
      System.out.println("Hey");
      return ((WebApplicationException) throwable).getResponse(); //the response is already prepared
    }
    return Response.status(Status.INTERNAL_SERVER_ERROR)
        .entity(throwable.getMessage())
        .build();
  }
}
