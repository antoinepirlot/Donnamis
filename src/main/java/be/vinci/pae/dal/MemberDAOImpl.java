package be.vinci.pae.dal;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.MemberDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAOImpl implements MemberDAO {

  private final DALServices dalServices = new DALServices();
  @Inject
  private Factory factory;

  //  /**
  //   * Get all members from the db.
  //   *
  //   * @return a list of members
  //   */
  //  @Override
  //  public List<MemberDTO> getAll() {
  //    System.out.println("getAll");
  //    List<MemberDTO> membersToReturn = new ArrayList<>();
  //    try {
  //      String query = "SELECT * FROM project_pae.members";
  //      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
  //      System.out.println("Préparation du statement");
  //      try (ResultSet rs = preparedStatement.executeQuery()) {
  //        while (rs.next()) {
  //          MemberDTO memberDTO = createMemberInstance(rs);
  //          membersToReturn.add(memberDTO);
  //        }
  //      }
  //    } catch (SQLException e) {
  //      System.out.println(e.getMessage());
  //    }
  //    System.out.println("Création des membres réussie");
  //
  //    return membersToReturn;
  //  }

  /**
   * Verify if the member is present into the db and its username and password are correct then it
   * created the token associated with this member if login credentials are correct.
   *
   * @param username the member's username
   * @param password the member's password
   * @return the match member otherwise null
   */
  @Override
  public MemberDTO getOne(String username, String password) {
    MemberDTO memberDTO = getOne(username);
    if (memberDTO == null) {
      throw new WebApplicationException(Response.status(Status.NOT_FOUND)
          .entity("Wrong password or username")
          .type("text/plain")
          .build());
    }
    return memberDTO;
  }

  /**
   * Get a specific member identified by its username.
   *
   * @param username the member's username
   * @return the member got from the db
   */
  private MemberDTO getOne(String username) {
    System.out.println("getOne(String username) in MemberDAO");
    String query = "SELECT * FROM project_pae.members WHERE username = ?";
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      System.out.println("Prepared statement successfully generated");
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
   * Create a Member instance.
   *
   * @param rs the result set that contains sql result
   * @return a new instance of member based on what rs contains
   * @throws SQLException if there's an issue while getting data from the result set
   */
  private MemberDTO createMemberInstance(ResultSet rs) throws SQLException {
    System.out.println("Member instance creation");
    MemberDTO memberDTO = factory.getMember();
    memberDTO.setId(rs.getInt("id_member"));
    memberDTO.setUsername(rs.getString("username"));
    memberDTO.setPassword(rs.getString("password"));
    memberDTO.setLastName(rs.getString("last_name"));
    memberDTO.setFirstName(rs.getString("first_name"));
    memberDTO.setAdmin(rs.getBoolean("is_admin"));
    memberDTO.setActualState(rs.getString("state"));
    memberDTO.setPhoneNumber(rs.getString("phone"));
    return memberDTO;
  }

  //  /**
  //   * Add the member to the db.
  //   *
  //   * @param memberDTO the member to add into the db
  //   * @return the added member
  //   */
  //  @Override
  //  public MemberDTO createOne(MemberDTO memberDTO) {
  //    String query = "INSERT INTO project_pae.members (username, password, last_name, first_name,"
  //        + "is_admin, state, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";
  //    try {
  //      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
  //      preparedStatement.setString(1, memberDTO.getUsername());
  //      preparedStatement.setString(2, memberDTO.getPassword());
  //      preparedStatement.setString(3, memberDTO.getLastName());
  //      preparedStatement.setString(4, memberDTO.getFirstName());
  //      preparedStatement.setBoolean(5, memberDTO.isAdmin());
  //      preparedStatement.setString(6, memberDTO.getActualState());
  //      preparedStatement.setString(7, memberDTO.getPhoneNumber());
  //      try (ResultSet rs = preparedStatement.executeQuery()) {
  //        //it adds into the db BUT can't execute getOne(), it returns null
  //        if (rs.next()) {
  //          System.out.println("Ajout du membre réussi.");
  //          return this.getOne(memberDTO.getUsername());
  //        }
  //      }
  //    } catch (SQLException e) {
  //      System.out.println(e.getMessage());
  //    }
  //    return null;
  //  }
  //

  //  /**
  //   * Add a new member to the db if it's not already in the db.
  //   *
  //   * @param username    the member's username
  //   * @param password    the member's password
  //   * @param lastName    the member's lastname
  //   * @param firstName   the member's firstname
  //   * @param actualState the member's actualState ("registered" while registering)
  //   * @param phoneNumber the member's phone number
  //   * @param admin       the member's admin status (false by default)
  //   * @return the new created member if it's not already into the db otherwise null
  //   */
  //  public ObjectNode register(String username, String password, String lastName,
  //  String firstName, String actualState, String phoneNumber, boolean admin) {
  //    MemberDTO tempMemberDTO = getOne(username);
  //    if (tempMemberDTO != null) { // the user already exists !
  //      return null;
  //    }
  //    tempMemberDTO = new MemberDTO(
  //        0,
  //        StringEscapeUtils.escapeHtml4(username),
  //        StringEscapeUtils.escapeHtml4(password),
  //        StringEscapeUtils.escapeHtml4(lastName),
  //        StringEscapeUtils.escapeHtml4(firstName),
  //        admin,
  //        StringEscapeUtils.escapeHtml4(actualState),
  //        StringEscapeUtils.escapeHtml4(phoneNumber)
  //    );
  //    System.out.println("!!!!!!!!!");
  //    System.out.println(tempMemberDTO);
  //    MemberDTO addedMemberDTO = this.createOne(tempMemberDTO);
  //    if (addedMemberDTO == null) {
  //      System.out.println("addedMember is null.");
  //      return null;
  //    }
  //    try {
  //      return createToken(addedMemberDTO);
  //    } catch (Exception e) {
  //      System.out.println("Unable to create token");
  //      return null;
  //    }
  //  }
}
