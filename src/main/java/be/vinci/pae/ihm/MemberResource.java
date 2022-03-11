package be.vinci.pae.ihm;

import be.vinci.pae.biz.MemberDTO;
import be.vinci.pae.biz.MemberUCC;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Root resource (exposed at "myresource" path).
 */
@Singleton
@Path("members")
public class MemberResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private MemberUCC memberUCC;

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as
   * "text/plain" media type.
   *
   * @return list of member
   */
  @GET
  @Path("list_member")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<MemberDTO> getAllMembers() {
    List<MemberDTO> listMemberDTO = memberUCC.getAllMembers();
    if (listMemberDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    try {
      return listMemberDTO;
    } catch (Exception e) {
      System.out.println("Unable to create list of member");
      return null;
    }
  }

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as
   * "text/plain" media type.
   *
   * @return list of member
   */
  @GET
  @Path("list_registered")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<MemberDTO> getMembersRegistered() {
    List<MemberDTO> listMemberDTO = memberUCC.getMembersRegistered();
    if (listMemberDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    try {
      return listMemberDTO;
    } catch (Exception e) {
      System.out.println("Unable to create list of member");
      return null;
    }
  }

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as
   * "text/plain" media type.
   *
   * @return list of member
   */
  @GET
  @Path("list_denied")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<MemberDTO> getMembersDenied() {
    List<MemberDTO> listMemberDTO = memberUCC.getMembersDenied();
    if (listMemberDTO == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    try {
      return listMemberDTO;
    } catch (Exception e) {
      System.out.println("Unable to create list of member");
      return null;
    }
  }

  /**
   * Method that login the member. It verify if the user can be connected by calling ucc.
   *
   * @param json the member login informations
   * @return token created for the member
   */
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
    String username = StringEscapeUtils.escapeHtml4(json.get("username").asText());
    String password = json.get("password").asText();
    MemberDTO memberDTO = memberUCC.login(username, password);
    String token = createToken(memberDTO.getId());
    return createObjectNode(token, memberDTO);
  }

  /**
   * Create a ObjectNode that contains a token from a String.
   *
   * @param token that will be add to ObjectNode
   * @return objectNode that contains the new token
   */
  private ObjectNode createObjectNode(String token, MemberDTO memberDTO) {
    try {
      return jsonMapper.createObjectNode()
          .put("token", token)
          .putPOJO("memberDTO", memberDTO);
    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }
  }

  /**
   * Create a connection token for a member.
   *
   * @return the member's token
   */
  private String createToken(int id) {
    System.out.println("Generating token.");
    Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
    Date date = new Date();
    long duration = 1000 * 60 * 60; //1 hour
    System.out.println("GetTime() : " + (date.getTime() + duration));
    System.out.println("Date" + new Date(date.getTime() + duration));
    return JWT.create()
        .withIssuer("auth0")
        .withClaim("id", id)
        .withExpiresAt(new Date(date.getTime() + duration))
        .sign(jwtAlgorithm);
  }

  //  @POST
  //  @Path("register")
  //  @Consumes(MediaType.APPLICATION_JSON)
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public ObjectNode register(JsonNode json) {
  //    // Get and check credentials
  //    if (!json.hasNonNull("username") || !json.hasNonNull("password") ||
  //        !json.hasNonNull("lastName") || !json.hasNonNull("firstName") ||
  //        !json.hasNonNull("actualState") || !json.hasNonNull("phoneNumber") ||
  //        !json.hasNonNull("admin")) {
  //      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
  //          .entity("username or password required").type("text/plain").build());
  //    }
  //    String username = json.get("username").asText();
  //    String password = json.get("password").asText();
  //    String lastName = json.get("lastName").asText();
  //    String firstName = json.get("firstName").asText();
  //    String actualState = json.get("actualState").asText();
  //    String phoneNumber = json.get("phoneNumber").asText();
  //    boolean admin = json.get("admin").asBoolean();
  //
  //    // Try to login
  //    ObjectNode publicUser = memberUCC.register(username, password, lastName, firstName,
  //        actualState, phoneNumber, admin);
  //    if (publicUser == null) {
  //      throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
  //          .entity("this resource already exists").type(MediaType.TEXT_PLAIN)
  //          .build());
  //    }
  //    return publicUser;
  //
  //  }
  //
  //  @POST
  //  @Produces(MediaType.APPLICATION_JSON)
  //  @Consumes(MediaType.APPLICATION_JSON)
  //  public MemberDTO createOne(MemberDTO memberDTO) {
  //    return memberUCC.createOne(memberDTO);
  //  }
}
