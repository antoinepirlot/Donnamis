package be.vinci.pae.dal;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.MemberDTO;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class MemberDAOImpl implements MemberDAO {

  @Inject
  private DALServices dalServices;
  @Inject
  private Factory factory;
  private static final String DEFAULT_STATE = "registered";
  private static final boolean DEFAULT_IS_ADMIN = false;

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
   * Get all the members in the DB.
   *
   * @return all the members otherwise null
   */
  public List<MemberDTO> getAllMembers() {
    List<MemberDTO> listMemberDTO = new ArrayList<>();
    String query = "SELECT * FROM project_pae.members";

    //Execute the query
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          listMemberDTO.add(createMemberInstance(rs));
        }
        return listMemberDTO;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("ici");
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
  @Override
  public MemberDTO getOne(String username, String password) {
    return getOne(username);
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

  //****************************** UTILS *******************************

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

  /**
   * Add a new member to the db if it's not already in the db.
   * @param memberDTO the memberDTO to add in the db
   * @return the new created member if it's not already into the db otherwise null
   */
    public boolean register(MemberDTO memberDTO) {
      MemberDTO memberDB = this.getOne(memberDTO.getUsername());
      if (memberDB != null) { // the user already exists !
        return false;
      }
      return addOne(memberDTO);
    }

  /**
   * Add the member to the db.
   *
   * @param memberDTO the member to add into the db
   * @return the added member
   */
  private boolean addOne(MemberDTO memberDTO) {
    String query = "INSERT INTO project_pae.members (username, password, last_name, first_name,"
        + "is_admin, state) VALUES (?, ?, ?, ?, ?, ?)";
    try {
      PreparedStatement ps = dalServices.getPreparedStatement(query);
      ps.setString(1, StringEscapeUtils.escapeHtml4(memberDTO.getUsername()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(memberDTO.getPassword()));
      ps.setString(3, StringEscapeUtils.escapeHtml4(memberDTO.getLastName()));
      ps.setString(4, StringEscapeUtils.escapeHtml4(memberDTO.getFirstName()));
      ps.setBoolean(5, DEFAULT_IS_ADMIN);
      ps.setString(6, DEFAULT_STATE);
      try (ResultSet rs = ps.executeQuery()) {
        //it adds into the db BUT can't execute getOne(), it returns null
        if (rs.next()) {
          System.out.println("Ajout du membre réussi.");
          return true;
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }
}
