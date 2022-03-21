package be.vinci.pae.ihm.member;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
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
   * Asks UCC to confirm the member identified by its id.
   *
   * @param id the member's id
   * @return the confirmed member
   */
  @PUT
  @Path("confirm/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MemberDTO confirmMember(@PathParam("id") int id) {
    System.out.println("********* Confirm Member *************");
    if (memberUCC.getOneMember(id) == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    return memberUCC.confirmMember(id);
  }

  /**
   * Asks UCC to confirm an admin identified by its id.
   *
   * @param id the member's id
   * @return the confirmed admin member
   */
  @PUT
  @Path("confirmAdmin/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MemberDTO confirmAdmin(@PathParam("id") int id) {
    if (memberUCC.getOneMember(id) == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    return memberUCC.confirmAdmin(id);
  }

  /**
   * Asks UCC to deny the member's inscription.
   *
   * @param id the member's id
   * @return the denied member
   */
  @PUT
  @Path("denies/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MemberDTO denyMember(@PathParam("id") int id) {
    if (memberUCC.getOneMember(id) == null) {
      throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
          .entity("Ressource not found").type("text/plain").build());
    }
    return memberUCC.denyMember(id);
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
    memberDTO.setPassword(null);
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

  /**
   * Asks UCC to register a member.
   *
   * @param memberDTO the member to register
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  public void register(MemberDTO memberDTO) {
    // Verify memberDTO integrity
    if (memberDTO == null
        || memberDTO.getUsername() == null || memberDTO.getUsername().equals("")
        || memberDTO.getPassword() == null || memberDTO.getPassword().equals("")
        || memberDTO.getFirstName() == null || memberDTO.getFirstName().equals("")
        || memberDTO.getLastName() == null || memberDTO.getLastName().equals("")
    ) {
      throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
          .entity("Missing member information")
          .type(MediaType.TEXT_PLAIN)
          .build());
    }
    //Verify addressDTO integrity
    AddressDTO addressDTO = memberDTO.getAddress();
    if (addressDTO == null
        || addressDTO.getStreet() == null || addressDTO.getStreet().equals("")
        || addressDTO.getCommune() == null || addressDTO.getCommune().equals("")
        || addressDTO.getPostcode() == null || addressDTO.getPostcode().equals("")
        || addressDTO.getBuildingNumber() == null || addressDTO.getBuildingNumber().equals("")
    ) {
      throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity("Missing address information")
          .type("text/plain")
          .build());
    }
    // Get and check credentials
    if (!memberUCC.register(memberDTO)) {
      throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
          .entity("this resource already exists").type(MediaType.TEXT_PLAIN)
          .build());
    }
  }
}
