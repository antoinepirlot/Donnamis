package be.vinci.pae.services;

import be.vinci.pae.domain.Member;
import be.vinci.pae.services.utils.Json;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class MemberDAO {

  private static final String COLLECTION_NAME = "members";
  private static Json<Member> jsonDB = new Json<>();
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final DALServices dalServices = new DALServices();

  /**
   * Get all members from the db.
   * @return a list of members
   */
  public List<Member> getAll() {
    //List<Member> members = jsonDB.parse(COLLECTION_NAME);
    List<Member> membersToReturn = new ArrayList<>();

    try {
      String query = "SELECT * FROM project_pae.members";
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          System.out.println("Création du membre : " + rs.getInt(1));
          Member member = new Member(rs.getInt(1), rs.getString(2),
              rs.getString(3), rs.getString(4), rs.getString(5),
              rs.getBoolean(6), rs.getString(7), rs.getString(8));
          System.out.println("Ajout du membre dans la liste des membres : " + member);
          membersToReturn.add(member);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Création des membres réussie");

    return membersToReturn;
  }

  /**
   * Get a specific member identified by its id
   * @param id the member's id (it is identified in the db with its id)
   * @return the member got in from the db
   */
  public Member getOne(int id) {
    try {
      String query = "SELECT * FROM project_pae.members WHERE id_member = ?";
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) { //We know only one is returned by the db
          System.out.println("Création du membre : " + rs.getInt(1));
          Member member = new Member(rs.getInt(1), rs.getString(2),
              rs.getString(3), rs.getString(4), rs.getString(5),
              rs.getBoolean(6), rs.getString(7), rs.getString(8));
          System.out.println("Ajout du membre dans la liste des membres : " + member);
          return member;
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    //List<Member> items = jsonDB.parse(COLLECTION_NAME);
    //return items.stream().filter(item -> item.getId() == id).findAny().orElse(null);
    return null;
  }

  /**
   * Get a specific member identified by its username.
   * @param username the member's username
   * @return the member got from the db
   */
  public Member getOne(String username) {
    try {
      String query = "SELECT * FROM project_pae.members WHERE username = ?";
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      preparedStatement.setString(1, username);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) { //We know only one is returned by the db
          System.out.println("Création du membre : " + rs.getInt(1));
          Member member = new Member(rs.getInt(1), rs.getString(2),
              rs.getString(3), rs.getString(4), rs.getString(5),
              rs.getBoolean(6), rs.getString(7), rs.getString(8));
          System.out.println("Ajout du membre dans la liste des membres : " + member);
          //membersToReturn.add(member);
          return member;
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
    /*
    var items = jsonDB.parse(COLLECTION_NAME);
    return items.stream().filter(item -> item.getUsername().equals(username)).findAny()
        .orElse(null);*/
  }

  /**
   * Create one member and add it to the json file.
   * @param member the member to add into the json file
   * @return the added member
   */
  public Member createOne(Member member) {
    List<Member> members = jsonDB.parse(COLLECTION_NAME);
    member.setId(nextMemberId());
    member.setUsername(StringEscapeUtils.escapeHtml4(member.getUsername()));
    member.setPassword(StringEscapeUtils.escapeHtml4(member.getPassword()));
    member.setLastName(StringEscapeUtils.escapeHtml4(member.getLastName()));
    member.setFirstName(StringEscapeUtils.escapeHtml4(member.getFirstName()));
    member.setActualState(StringEscapeUtils.escapeHtml4(member.getActualState()));
    member.setPhoneNumber(StringEscapeUtils.escapeHtml4(member.getPhoneNumber()));

    members.add(member);
    jsonDB.serialize(members, COLLECTION_NAME);
    return member;
  }

  /**
   * Verify if the member is present into the db and its username and password are correct
   * then it created the token associated with this member if login credentials are correct.
   * @param username the member's username
   * @param password the member's password
   * @return the match member otherwise null
   */
  public ObjectNode login(String username, String password) {
    Member member = getOne(username);
    if (member == null || !member.checkPassword(password)) {
      return null;
    }
    try {
      return createToken(member);
    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }
  }

  /**
   * Add a new member to the db if it's not already in the db.
   * @param username the member's username
   * @param password the member's password
   * @param lastName the member's lastname
   * @param firstName the member's firstname
   * @param actualState the member's actualState ("registered" while registering)
   * @param phoneNumber the member's phone number
   * @param admin the member's admin status (false by default)
   * @return the new created member if it's not already into the db otherwise null
   */
  public ObjectNode register(String username, String password, String lastName, String firstName,
      String actualState, String phoneNumber, boolean admin) {
    Member tempMember = getOne(username);
    if (tempMember != null) { // the user already exists !
      return null;
    }
    tempMember = new Member();
    tempMember.setUsername(username);
    tempMember.setPassword(password);
    tempMember.setLastName(lastName);
    tempMember.setFirstName(firstName);
    tempMember.setActualState(actualState);
    tempMember.setPhoneNumber(phoneNumber);
    tempMember.setAdmin(admin);

    Member member = createOne(tempMember);
    if (member == null) {
      return null;
    }
    try {
      return createToken(member);
    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }
  }

  /**
   * Create a connection token for a member
   * @param member the member who need a token
   * @return the member's token
   * @throws Exception
   */
  private ObjectNode createToken(Member member) throws Exception {
    String token;
    token = JWT.create().withIssuer("auth0")
        .withClaim("member", member.getId()).sign(this.jwtAlgorithm);
    return jsonMapper.createObjectNode()
        .put("token", token)
        .put("id", member.getId())
        .put("username", member.getUsername());
  }

  /**
   * Look for the next member id based on the last added member.
   * @return 1 if there's no member otherwise the id for the new member
   */
  private int nextMemberId() {
    List<Member> members = jsonDB.parse(COLLECTION_NAME);

    if (members.size() == 0) {
      return 1;
    }
    return members.get(members.size() - 1).getId() + 1;
  }
}

