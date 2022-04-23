package be.vinci.pae.ihm.member;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeAdmin;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.filter.utils.Json;
import be.vinci.pae.ihm.utils.TokenDecoder;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path).
 */
@Singleton
@Path("members")
public class MemberResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final Json<MemberDTO> jsonUtil = new Json<>(MemberDTO.class);
  @Inject
  private MemberUCC memberUCC;

  /////////////////////////////////////////////////////////
  ///////////////////////HEAD//////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Asks UCC to check if the member is admin or not.
   */
  @HEAD
  @Path("is_admin")
  @Produces(MediaType.TEXT_PLAIN)
  @AuthorizeAdmin
  public void isAdmin() {
    //@AuthorizeAdmin checks if the member is admin or not
  }

  /////////////////////////////////////////////////////////
  ///////////////////////GET///////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as
   * "text/plain" media type.
   *
   * @return list of member
   */
  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public List<MemberDTO> getAllMembers() throws SQLException {
    List<MemberDTO> listMemberDTO = memberUCC.getAllMembers();
    if (listMemberDTO == null || listMemberDTO.isEmpty()) {
      throw new ObjectNotFoundException("No member into the database");
    }
    return this.jsonUtil.filterPublicJsonViewAsList(listMemberDTO);
  }

  @GET
  @Path("/me")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode refreshToken(@Context ContainerRequestContext request) throws SQLException {
    DecodedJWT decodedJWT = TokenDecoder.decodeToken(request);
    MemberDTO memberDTO = this.memberUCC.getOneMember(decodedJWT.getClaim("id").asInt());
    if (memberDTO == null) {
      String message = "This member has not been found.";
      throw new ObjectNotFoundException(message);
    }
    String token = this.createToken(memberDTO.getId());
    return this.createObjectNode(token, memberDTO);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public MemberDTO getMemberById(@PathParam("id") int id) throws SQLException {
    MemberDTO memberDTO = memberUCC.getOneMember(id);
    if (memberDTO == null) {
      throw new ObjectNotFoundException("Member not found");
    }
    return this.jsonUtil.filterPublicJsonView(memberDTO);
  }

  @GET
  @Path("interested/{idOffer}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<MemberDTO> getInterestedMembers(@PathParam("idOffer") int idOffer)
      throws SQLException {
    if (idOffer < 1) {
      throw new WrongBodyDataException("The idOffer is less than 1");
    }
    return this.jsonUtil.filterPublicJsonViewAsList(this.memberUCC.getInterestedMembers(idOffer));
  }

  /////////////////////////////////////////////////////////
  ///////////////////////POST//////////////////////////////
  /////////////////////////////////////////////////////////

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
  public ObjectNode login(MemberDTO memberDTO) throws SQLException {
    // Get and check credentials
    if (memberDTO == null
        || memberDTO.getUsername() == null
        || memberDTO.getPassword() == null
    ) {
      String message = "Member has wrong attributes for login method";
      throw new WrongBodyDataException(message);
    }
    memberDTO = memberUCC.login(memberDTO);
    if (memberDTO == null) {
      throw new ObjectNotFoundException("Member is null");
    }
    String token = createToken(memberDTO.getId());
    return createObjectNode(token, memberDTO);
  }

  /**
   * Asks UCC to register a member.
   *
   * @param memberDTO the member to register
   */
  @POST
  @Path("register")
  @Consumes(MediaType.APPLICATION_JSON)
  public void register(MemberDTO memberDTO) throws SQLException {
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

  /////////////////////////////////////////////////////////
  ///////////////////////PUT///////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Asks UCC to confirm the member identified by its id.
   *
   * @param memberDTO the member to confirm
   */
  @PUT
  @Path("confirm")
  @Consumes(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public void confirmMember(MemberDTO memberDTO) throws SQLException, UnexpectedException {
    if (memberDTO == null || memberDTO.getId() < 1) {
      throw new WrongBodyDataException("Confirmation of a incomplete member's information.");
    }
    if (memberUCC.getOneMember(memberDTO.getId()) == null) {
      throw new ObjectNotFoundException("No member with the id: " + memberDTO.getId());
    }
    if (!memberUCC.confirmMember(memberDTO)) {
      throw new UnexpectedException("An unexpected error happened while confirming member.");
    }
  }

  /**
   * Asks UCC to deny the member's inscription.
   *
   * @param refusalDTO the refusal information
   */
  @PUT
  @Path("deny")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeAdmin
  public void denyMember(RefusalDTO refusalDTO) throws SQLException, UnexpectedException {
    if (!memberUCC.memberExist(refusalDTO.getMember(), -1)) {
      throw new ObjectNotFoundException("No member with the id: " + refusalDTO.getMember().getId());
    }
    if (!memberUCC.denyMember(refusalDTO)) {
      throw new UnexpectedException("An unexpected error happened while denying member.");
    }
  }

  @PUT
  @Path("modify")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public MemberDTO modifyMember(MemberDTO memberDTO) throws SQLException {
    if (memberDTO == null
        || memberDTO.getUsername() == null || memberDTO.getUsername().isBlank()
        || memberDTO.getFirstName() == null || memberDTO.getFirstName().isBlank()
    ) {
      throw new WrongBodyDataException("Member incomplete");
    }
    MemberDTO modifyMember = memberUCC.modifyMember(memberDTO);
    if (modifyMember == null) {
      throw new ObjectNotFoundException("Member not found");
    }
    return modifyMember;
  }

  /////////////////////////////////////////////////////////
  ///////////////////////UTILS/////////////////////////////
  /////////////////////////////////////////////////////////

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
    long duration = 1000 * 60 * 60 * 48; //2 days
    return JWT.create()
        .withIssuer("auth0")
        .withClaim("id", id)
        .withExpiresAt(new Date(date.getTime() + duration))
        .sign(jwtAlgorithm);
  }
}
