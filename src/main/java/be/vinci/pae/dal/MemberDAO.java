package be.vinci.pae.dal;

import be.vinci.pae.biz.Member;
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

  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final DALServices dalServices = new DALServices();

  /**
   * Get all members from the db.
   *
   * @return a list of members
   */
  public List<Member> getAll() {
    System.out.println("getAll");
    List<Member> membersToReturn = new ArrayList<>();
    try {
      String query = "SELECT * FROM project_pae.members";
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          Member member = createMemberInstance(rs);
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
   *
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
          return createMemberInstance(rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * Get a specific member identified by its username.
   *
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
          return createMemberInstance(rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * Create a Member instance
   *
   * @param rs the result set that contains sql result
   * @return a new instance of member based on what rs contains
   * @throws SQLException if there's an issue while getting data from the result set
   */
  private Member createMemberInstance(ResultSet rs) throws SQLException {
    int id = rs.getInt("id_member");
    System.out.println("Création du membre : " + id);
    Member member = new Member(
        id, rs.getString("username"),
        rs.getString("password"), rs.getString("last_name"),
        rs.getString("first_name"), rs.getBoolean("is_admin"),
        rs.getString("state"), rs.getString("phone")
    );
    System.out.println("Ajout du membre dans la liste des membres : " + member);
    return member;
  }

  /**
   * Add the member to the db.
   *
   * @param member the member to add into the db
   * @return the added member
   */
  public Member createOne(Member member) {
    String query = "INSERT INTO project_pae.members (username, password, last_name, first_name, "
        + "is_admin, state, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try {
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      preparedStatement.setString(1, member.getUsername());
      preparedStatement.setString(2, member.getPassword());
      preparedStatement.setString(3, member.getLastName());
      preparedStatement.setString(4, member.getFirstName());
      preparedStatement.setBoolean(5, member.isAdmin());
      preparedStatement.setString(6, member.getActualState());
      preparedStatement.setString(7, member.getPhoneNumber());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        //it adds into the db BUT can't execute getOne(), it returns null
        if (rs.next()) {
          System.out.println("Ajout du membre réussi.");
          return this.getOne(member.getUsername());
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * Verify if the member is present into the db and its username and password are correct then it
   * created the token associated with this member if login credentials are correct.
   *
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
   *
   * @param username    the member's username
   * @param password    the member's password
   * @param lastName    the member's lastname
   * @param firstName   the member's firstname
   * @param actualState the member's actualState ("registered" while registering)
   * @param phoneNumber the member's phone number
   * @param admin       the member's admin status (false by default)
   * @return the new created member if it's not already into the db otherwise null
   */
  public ObjectNode register(String username, String password, String lastName, String firstName,
      String actualState, String phoneNumber, boolean admin) {
    Member tempMember = getOne(username);
    if (tempMember != null) { // the user already exists !
      return null;
    }
    tempMember = new Member(
        0,
        StringEscapeUtils.escapeHtml4(username),
        StringEscapeUtils.escapeHtml4(password),
        StringEscapeUtils.escapeHtml4(lastName),
        StringEscapeUtils.escapeHtml4(firstName),
        admin,
        StringEscapeUtils.escapeHtml4(actualState),
        StringEscapeUtils.escapeHtml4(phoneNumber)
    );
    System.out.println("!!!!!!!!!");
    System.out.println(tempMember);
    Member addedMember = this.createOne(tempMember);
    if (addedMember == null) {
      System.out.println("addedMember is null.");
      return null;
    }
    try {
      return createToken(addedMember);
    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }
  }

  /**
   * Create a connection token for a member
   *
   * @param member the member who need a token
   * @return the member's token
   */
  private ObjectNode createToken(Member member) {
    String token;
    token = JWT.create().withIssuer("auth0")
        .withClaim("member", member.getId()).sign(this.jwtAlgorithm);
    return jsonMapper.createObjectNode()
        .put("token", token)
        .put("id", member.getId())
        .put("username", member.getUsername());
  }

}

