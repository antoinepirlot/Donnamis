package be.vinci.pae.api;

import be.vinci.pae.domain.Member;
import be.vinci.pae.services.MemberDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Singleton
@Path("members")
public class MemberResource {

  private MemberDAO myMemberDAO = new MemberDAO();

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as
   * "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Member> getAll() throws SQLException {
    return myMemberDAO.getAll();
  }


  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("username") || !json.hasNonNull("password")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("username or password required").type("text/plain").build());
    }
    String username = json.get("username").asText();
    String password = json.get("password").asText();

    // Try to login
    ObjectNode publicUser = myMemberDAO.login(username, password);
    if (publicUser == null) {
      throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
          .entity("username or password incorrect").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return publicUser;
  }


  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode register(JsonNode json) {
    // Get and check credentials
    if (!json.hasNonNull("username") || !json.hasNonNull("password") ||
        !json.hasNonNull("lastName") || !json.hasNonNull("firstName") ||
        !json.hasNonNull("actualState") || !json.hasNonNull("phoneNumber") ||
        !json.hasNonNull("admin")) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("username or password required").type("text/plain").build());
    }
    String username = json.get("username").asText();
    String password = json.get("password").asText();
    String lastName = json.get("lastName").asText();
    String firstName = json.get("firstName").asText();
    String actualState = json.get("actualState").asText();
    String phoneNumber = json.get("phoneNumber").asText();
    boolean admin = json.get("admin").asBoolean();

    // Try to login
    ObjectNode publicUser = myMemberDAO.register(username, password, lastName, firstName,
        actualState, phoneNumber, admin);
    if (publicUser == null) {
      throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
          .entity("this resource already exists").type(MediaType.TEXT_PLAIN)
          .build());
    }
    return publicUser;

  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Member createOne(Member member) {
    return myMemberDAO.createOne(member);
  }

}