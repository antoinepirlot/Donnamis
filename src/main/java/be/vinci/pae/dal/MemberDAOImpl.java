package be.vinci.pae.dal;

import be.vinci.pae.biz.AddressDTO;
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
    return null;
  }

  /**
   * Get a specific member identified by its username.
   *
   * @param username the member's username
   * @return the member got from the db
   */
  @Override
  public MemberDTO getOne(String username) {
    System.out.println("getOne(String username) in MemberDAO");
    String query = "SELECT m.id_member, m.username, m.password, m.last_name, m.first_name, "
        + "m.is_admin, m.state, m.phone, a.id_address, a.street, a.building_number, a.unit_number,"
        + "a.postcode, a.commune "
        + "FROM project_pae.members m, project_pae.addresses a "
        + "WHERE m.username = ?";
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
   * Load the member's id and set it into memberDTO
   *
   * @param memberDTO the member DTO that needs the id being loaded
   */
  private void loadMemberId(MemberDTO memberDTO) {
    String query = "SELECT id_member FROM project_pae.members WHERE username = ?";
    try (
        PreparedStatement ps = dalServices.getPreparedStatement(query)
    ) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(memberDTO.getUsername()));
      try (
          ResultSet rs = ps.executeQuery()
      ) {
        if(rs.next()) {
          memberDTO.setId(rs.getInt("id_member"));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
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
    memberDTO.setAddress(this.createAddressInstance(rs));
    return memberDTO;
  }

  /**
   * Create an address instance.
   *
   * @param rs the result set that contains sql result
   * @return a new instance of address based on what rs contains
   * @throws SQLException if there's an issue while getting data from the result set
   */
  private AddressDTO createAddressInstance(ResultSet rs) throws SQLException {
    System.out.println("Address instance creation");
    AddressDTO addressDTO = factory.getAddress();
    addressDTO.setId(rs.getInt("id_address"));
    addressDTO.setStreet(rs.getString("street"));
    addressDTO.setBuildingNumber(rs.getString("building_number"));
    addressDTO.setUnitNumber(rs.getString("unit_number"));
    addressDTO.setPostcode(rs.getString("postcode"));
    addressDTO.setCommune(rs.getString("commune"));
    addressDTO.setId(rs.getInt("id_member"));
    return addressDTO;
  }

  /**
   * Add a new member to the db if it's not already in the db, then its address
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been  registered
   */
  public boolean register(MemberDTO memberDTO) {
    MemberDTO memberDB = this.getOne(memberDTO.getUsername());
    if (memberDB != null) { // the user already exists !
      return false;
    }
    //Add the member to the db then the address
    if (!addOne(memberDTO)) {
      return false;
    }
    loadMemberId(memberDTO);
    return addAddress(memberDTO.getId(), memberDTO.getAddress());
  }

  /**
   * Add the member to the db.
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been added to the DB
   */
  private boolean addOne(MemberDTO memberDTO) {
    String query = "INSERT INTO project_pae.members (username, password, last_name, first_name, "
        + "is_admin, state) VALUES (?, ?, ?, ?, ?, ?)";
    try (
        PreparedStatement ps = dalServices.getPreparedStatement(query)
    ) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(memberDTO.getUsername()));
      ps.setString(2, memberDTO.getPassword());
      ps.setString(3, StringEscapeUtils.escapeHtml4(memberDTO.getLastName()));
      ps.setString(4, StringEscapeUtils.escapeHtml4(memberDTO.getFirstName()));
      ps.setBoolean(5, DEFAULT_IS_ADMIN);
      ps.setString(6, DEFAULT_STATE);
      int result = ps.executeUpdate();
      //it adds into the db BUT can't execute getOne(), it returns null
      if (result != 0) {
        System.out.println("Ajout du membre réussi.");
        return true;
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  /**
   * Add the address associated with the member'id to the DB
   *
   * @param addressDTO the address to add into the db
   * @return true if the address has been added, otherwise false
   */
  private boolean addAddress(int memberId, AddressDTO addressDTO) {
    String query = "INSERT INTO project_pae.addresses (street, building_number, unit_number, "
        + "postcode, commune, id_member) "
        + "VALUES (?, ?, ?, ?, ?, ?);";
    try (
        PreparedStatement ps = dalServices.getPreparedStatement(query)
    ) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(addressDTO.getStreet()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(addressDTO.getBuildingNumber()));
      ps.setString(3, StringEscapeUtils.escapeHtml4(addressDTO.getUnitNumber()));
      ps.setString(4, StringEscapeUtils.escapeHtml4(addressDTO.getPostcode()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(addressDTO.getCommune()));
      ps.setInt(6, memberId);
      int result = ps.executeUpdate();
      if (result != 0) {
        System.out.println("Ajout de l'adresse réussi");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
