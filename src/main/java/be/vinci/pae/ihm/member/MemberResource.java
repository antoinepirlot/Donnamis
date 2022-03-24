package be.vinci.pae.ihm.member;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.utils.exceptions.ConflictException;
import be.vinci.pae.utils.exceptions.ObjectNotFoundException;
import be.vinci.pae.utils.exceptions.WrongBodyDataException;
import be.vinci.pae.utils.Config;
import be.vinci.pae.ihm.logs.LoggerHandler;
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
import jakarta.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Root resource (exposed at "myresource" path).
 */
@Singleton
@Path("members")
public class MemberResource {

  private final Logger logger = LoggerHandler.getLogger();

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
    try {
      List<MemberDTO> listMemberDTO = memberUCC.getAllMembers();
      if (listMemberDTO == null) {
        throw new ObjectNotFoundException("No member into the database");
      }
      return listMemberDTO;
    } catch (Exception e) {
      this.logger.log(Level.SEVERE, e.getMessage());
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
    try {
      List<MemberDTO> registeredMembers = memberUCC.getMembersRegistered();
      if (registeredMembers == null) {
        String message = "Get all registered members but there's no registered members.";
        throw new ObjectNotFoundException(message);
      }
      return registeredMembers;
    } catch (Exception e) {
      this.logger.log(Level.SEVERE, e.getMessage());
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
    try {
      List<MemberDTO> deniedMembers = memberUCC.getMembersDenied();
      if (deniedMembers == null) {
        String message = "Get all denied members but there's no denied members.";
        throw new ObjectNotFoundException(message);
      }
      return deniedMembers;
    } catch (Exception e) {
      this.logger.log(Level.SEVERE, e.getMessage());
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
    try {
      System.out.println("********* Confirm Member *************");
      if (memberUCC.getOneMember(id) == null) {
        throw new ObjectNotFoundException("No member with the id: " + id);
      }
      return memberUCC.confirmMember(id);
    } catch (ObjectNotFoundException ignored) {

    } catch (Exception e) {
      this.logger.log(Level.INFO, e.getMessage());
    }
    return null;
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
      String message = "Try to confirm an admin member with an id not in the database. Id: " + id;
      throw new ObjectNotFoundException(message);
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
      throw new ObjectNotFoundException("No member with the id: " + id);
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
    try {
      if (!json.hasNonNull("username") || !json.hasNonNull("password")) {
        throw new WrongBodyDataException("Member has wrong attributes for login method");
      }
      String username = StringEscapeUtils.escapeHtml4(json.get("username").asText());
      String password = json.get("password").asText();
      MemberDTO memberDTO = memberUCC.login(username, password);
      memberDTO.setPassword(null);
      String token = createToken(memberDTO.getId());
      return createObjectNode(token, memberDTO);
    } catch (Exception e) {
      this.logger.log(Level.SEVERE, e.getMessage());
    }
    return null;
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
      LoggerHandler.getLogger().log(Level.SEVERE, e.getMessage());
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
      String message = "Member miss some informations for registration";
      throw new WrongBodyDataException(message);
    }
    //Verify addressDTO integrity
    AddressDTO addressDTO = memberDTO.getAddress();
    if (addressDTO == null
        || addressDTO.getStreet() == null || addressDTO.getStreet().equals("")
        || addressDTO.getCommune() == null || addressDTO.getCommune().equals("")
        || addressDTO.getPostcode() == null || addressDTO.getPostcode().equals("")
        || addressDTO.getBuildingNumber() == null || addressDTO.getBuildingNumber().equals("")
    ) {
      String message = "Member has complete information but doesn't have "
          + "complete address information";
      throw new WrongBodyDataException(message);
    }
    // Get and check credentials
    if (!memberUCC.register(memberDTO)) {
      String message = "This member already exist in the database";
      throw new ConflictException(message);
    }
  }
}
