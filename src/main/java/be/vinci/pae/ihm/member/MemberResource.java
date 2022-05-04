package be.vinci.pae.ihm.member;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.exceptions.FatalException;
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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
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
  public List<MemberDTO> getAllMembers() {
    List<MemberDTO> listMemberDTO = memberUCC.getAllMembers();
    if (listMemberDTO == null) {
      throw new ObjectNotFoundException("No member into the database");
    }
    return this.jsonUtil.filterPublicJsonViewAsList(listMemberDTO);
  }

  /**
   * Check if the token is valid. If it's valid create new one.
   *
   * @param request the request that contains the token
   * @return a new token
   */
  @GET
  @Path("/me")
  @Produces(MediaType.APPLICATION_JSON)
  public ObjectNode refreshToken(@Context ContainerRequestContext request) {
    DecodedJWT decodedJWT = TokenDecoder.decodeToken(request);
    MemberDTO memberDTO = this.memberUCC.getOneMember(decodedJWT.getClaim("id").asInt());
    if (memberDTO == null) {
      String message = "This member has not been found.";
      throw new ObjectNotFoundException(message);
    }
    String token = this.createToken(memberDTO.getId());
    return this.createObjectNode(token, memberDTO);
  }

  /**
   * get a member with its id.
   *
   * @param id the member's id
   * @return the member that match with the id.
   */
  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public MemberDTO getMemberById(@PathParam("id") int id) {
    MemberDTO memberDTO = memberUCC.getOneMember(id);
    if (memberDTO == null) {
      throw new ObjectNotFoundException("Member not found");
    }
    return this.jsonUtil.filterPublicJsonView(memberDTO);
  }

  /**
   * Get a list of interested members for an offer identified by the offer's id.
   *
   * @param idOffer the offer's id
   * @return the list of interested member for the offer
   */
  @GET
  @Path("interested/{idOffer}")
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public List<MemberDTO> getInterestedMembers(@PathParam("idOffer") int idOffer) {
    if (idOffer < 1) {
      throw new WrongBodyDataException("The idOffer is less than 1");
    }
    List<MemberDTO> memberDTO = this.memberUCC.getInterestedMembers(idOffer);
//    if (memberDTO == null) {
//      throw new ObjectNotFoundException("Member not found with idOffer: " + idOffer);
//    }
    return this.jsonUtil.filterPublicJsonViewAsList(memberDTO);
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
  public ObjectNode login(MemberDTO memberDTO) {
    // Get and check credentials
    if (memberDTO == null
        || memberDTO.getUsername().isBlank()
        || memberDTO.getPassword().isBlank()
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
  public void register(MemberDTO memberDTO) {
    // Verify memberDTO integrity
    if (memberDTO == null
        || memberDTO.getUsername() == null || memberDTO.getUsername().isBlank()
        || memberDTO.getPassword() == null || memberDTO.getPassword().isBlank()
        || memberDTO.getFirstName() == null || memberDTO.getFirstName().isBlank()
        || memberDTO.getLastName() == null || memberDTO.getLastName().isBlank()
    ) {
      String message = "Member miss some informations for registration";
      throw new WrongBodyDataException(message);
    }
    //Verify addressDTO integrity
    AddressDTO addressDTO = memberDTO.getAddress();
    if (addressDTO == null
        || addressDTO.getStreet() == null || addressDTO.getStreet().isBlank()
        || addressDTO.getCommune() == null || addressDTO.getCommune().isBlank()
        || addressDTO.getPostcode() == null || addressDTO.getPostcode().isBlank()
        || addressDTO.getBuildingNumber() == null || addressDTO.getBuildingNumber().isBlank()
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
  public void confirmMember(MemberDTO memberDTO) {
    if (memberDTO == null || memberDTO.getId() < 1) {
      throw new WrongBodyDataException("Confirmation of a incomplete member's information.");
    }
    if (memberUCC.getOneMember(memberDTO.getId()) == null) {
      throw new ObjectNotFoundException("No member with the id: " + memberDTO.getId());
    }

    if (memberDTO.getVersion() != memberUCC.getOneMember(memberDTO.getId()).getVersion()) {
      throw new FatalException("Error with version");
    }

    if (!memberUCC.confirmMember(memberDTO)) {
      throw new FatalException("An unexpected error happened while confirming member.");
    }
  }

  /**
   * Asks UCC to set the state of the member to unavailable or confirmed.
   *
   * @param memberDTO the member to set unavailable
   */
  @PUT
  @Path("availability")
  @Consumes(MediaType.APPLICATION_JSON)
  public void setMemberAvailability(MemberDTO memberDTO) {
    if (memberDTO == null || memberDTO.getId() < 1) {
      throw new WrongBodyDataException("Error Member Sent");
    }
    if (memberUCC.getOneMember(memberDTO.getId()) == null) {
      throw new ObjectNotFoundException("Any member with the id: " + memberDTO.getId());
    }

    if (memberDTO.getVersion() != memberUCC.getOneMember(memberDTO.getId()).getVersion()) {
      throw new FatalException("Error with version");
    }

    if (!memberUCC.setMemberAvailability(memberDTO)) {
      throw new FatalException("An unexpected error happened while set member availability.");
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
  public void denyMember(RefusalDTO refusalDTO) {
    if (!memberUCC.memberExist(refusalDTO.getMember(), -1)) {
      throw new ObjectNotFoundException("No member with the id: " + refusalDTO.getMember().getId());
    }

    if (!memberUCC.denyMember(refusalDTO)) {
      throw new FatalException("An unexpected error happened while denying member.");
    }
  }

  /**
   * Modify the member identified by its id.
   *
   * @param memberDTO the new member
   * @return the member or null if there's no member with the id
   */
  @PUT
  @Path("modify")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public MemberDTO modifyMember(MemberDTO memberDTO) {
    if (memberDTO == null
        || memberDTO.getUsername() == null || memberDTO.getUsername().isBlank()
        || memberDTO.getFirstName() == null || memberDTO.getFirstName().isBlank()
    ) {
      throw new WrongBodyDataException("Member incomplete");
    }

    if (memberDTO.getVersion() != memberUCC.getOneMember(memberDTO.getId()).getVersion()) {
      throw new FatalException("Error with version");
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
