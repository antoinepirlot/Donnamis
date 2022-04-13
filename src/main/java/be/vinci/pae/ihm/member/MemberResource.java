package be.vinci.pae.ihm.member;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeAdmin;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.logs.LoggerHandler;
import be.vinci.pae.ihm.utils.Json;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

/**
 * Root resource (exposed at "myresource" path).
 */
@Singleton
@Path("members")
public class MemberResource {

  private final Logger logger = LoggerHandler.getLogger();
  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final Json<MemberDTO> jsonUtil = new Json<>(MemberDTO.class);

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
  @AuthorizeAdmin
  public List<MemberDTO> getAllMembers() {
    List<MemberDTO> listMemberDTO = memberUCC.getAllMembers();
    if (listMemberDTO == null) {
      String message = "No member into the database";
      this.logger.log(Level.INFO, message);
      throw new ObjectNotFoundException();
    }
    return this.jsonUtil.filterPublicJsonViewAsList(listMemberDTO);
  }

  @GET
  @Path("profil/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public MemberDTO getMemberById(@PathParam("id") int id) {
    MemberDTO memberDTO = memberUCC.getOneMember(id);
    if (memberDTO == null) {
      throw new ObjectNotFoundException("Member not found");
    }
    AddressDTO addressDTO = memberUCC.getAddressMember(id);
    if (addressDTO == null) {
      throw new ObjectNotFoundException("Address not found");
    }
    memberDTO.setAddress(addressDTO);
    return memberDTO;
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
  @AuthorizeAdmin
  public List<MemberDTO> getMembersRegistered() {
    List<MemberDTO> registeredMembers = memberUCC.getMembersRegistered();
    if (registeredMembers == null) {
      String message = "Get all registered members but there's no registered members.";
      throw new ObjectNotFoundException(message);
    }
    return this.jsonUtil.filterPublicJsonViewAsList(registeredMembers);
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
  @AuthorizeAdmin
  public List<MemberDTO> getMembersDenied() {
    List<MemberDTO> deniedMembers = memberUCC.getMembersDenied();
    if (deniedMembers == null) {
      String message = "Get all denied members but there's no denied members.";
      throw new ObjectNotFoundException(message);
    }
    return this.jsonUtil.filterPublicJsonViewAsList(deniedMembers);
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
  @AuthorizeAdmin
  public MemberDTO confirmMember(@PathParam("id") int id) {
    System.out.println("********* Confirm Member *************");
    if (memberUCC.getOneMember(id) == null) {
      throw new ObjectNotFoundException("No member with the id: " + id);
    }
    MemberDTO confirmedMember = memberUCC.confirmMember(id);
    if (confirmedMember == null) {
      String message = "Issue while confirming member.";
      throw new NullPointerException(message);
    }
    return this.jsonUtil.filterPublicJsonView(confirmedMember);
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
  @AuthorizeAdmin
  public MemberDTO confirmAdmin(@PathParam("id") int id) {
    if (memberUCC.getOneMember(id) == null) {
      String message = "Try to confirm an admin member with an id not in the database. Id: " + id;
      throw new ObjectNotFoundException(message);
    }
    MemberDTO confirmedAdmin = memberUCC.confirmAdmin(id);
    return this.jsonUtil.filterPublicJsonView(confirmedAdmin);
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
  @AuthorizeAdmin
  public MemberDTO denyMember(@PathParam("id") int id, String refusalText) {
    if (memberUCC.getOneMember(id) == null) {
      throw new ObjectNotFoundException("No member with the id: " + id);
    }
    MemberDTO memberDTO = memberUCC.denyMember(id, refusalText);
    return this.jsonUtil.filterPublicJsonView(memberDTO);
  }

  /**
   * Method that login the member. It verify if the user can be connected by calling ucc.
   *
   * @param memberDTO the member login informations
   * @return token created for the member
   */
  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode login(MemberDTO memberDTO) {
    // Get and check credentials
    if (memberDTO == null
        || memberDTO.getUsername() == null
        || memberDTO.getPassword() == null
    ) {
      String message = "Member has wrong attributes for login method";
      this.logger.log(Level.SEVERE, message);
      throw new WrongBodyDataException(message);
    }
    memberDTO = memberUCC.login(memberDTO);
    if (memberDTO == null) {
      throw new ObjectNotFoundException();
    }
    String token = createToken(memberDTO.getId());
    return createObjectNode(token, memberDTO);
  }

  /**
   * Asks UCC to check if the member is admin or not.
   *
   * @return null (the text is return with)
   */
  @GET
  @Path("is_admin")
  @Produces(MediaType.TEXT_PLAIN)
  @AuthorizeAdmin
  public String isAdmin() {
    return "This member is admin";
  }

  /**
   * Create a ObjectNode that contains a token from a String.
   *
   * @param token that will beBase64.decode(payload); add to ObjectNode
   * @return objectNode that contains the new token
   */
  private ObjectNode createObjectNode(String token, MemberDTO memberDTO) {
    return jsonMapper.createObjectNode()
        .put("token", token)
        .putPOJO("memberDTO", this.jsonUtil
            .filterPublicJsonView(memberDTO)
        );
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
