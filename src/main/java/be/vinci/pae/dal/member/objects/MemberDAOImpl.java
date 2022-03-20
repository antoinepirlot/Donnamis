package be.vinci.pae.dal.member.objects;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
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

  /**
   * Get all the members in the DB.
   *
   * @return all the members otherwise null
   */
  public List<MemberDTO> getAllMembers() {
    List<MemberDTO> listMemberDTO = new ArrayList<>();
    String query = "SELECT * FROM project_pae.members";

    //Execute the query
    return getMemberDTOS(listMemberDTO, query);
  }

  /**
   * Get all the members in the DB with the state registered.
   *
   * @return all the members with the state registered otherwise null
   */
  public List<MemberDTO> getMembersRegistered() {
    List<MemberDTO> listMemberDTO = new ArrayList<>();
    String query = "SELECT * FROM project_pae.members m WHERE m.state = 'registered'";

    //Execute the query
    return getMemberDTOS(listMemberDTO, query);
  }

  /**
   * Get all the members in the DB with the state denied.
   *
   * @return all the members with the state denied otherwise null
   */
  public List<MemberDTO> getMembersDenied() {
    List<MemberDTO> listMemberDTO = new ArrayList<>();
    String query = "SELECT * FROM project_pae.members m WHERE m.state = 'denied'";

    //Execute the query
    return getMemberDTOS(listMemberDTO, query);
  }

  /**
   * Get the members in the DB with the corresponding id.
   *
   * @param id the id of the member
   * @return the members in the DB with the corresponding id otherwise null
   */
  public MemberDTO getOneMember(int id) {
    String query = "SELECT * FROM project_pae.members m WHERE m.id_member = ?";
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return createMemberInstance(rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  /**
   * Change the state of the member to confirmed.
   *
   * @param id the id of the member
   * @return boolean
   */
  public MemberDTO confirmMember(int id) {
    String query = "UPDATE project_pae.members SET state = 'confirmed' WHERE id_member = ? "
        + "RETURNING *;";
    return executeQueryWithId(id, query);
  }

  /**
   * Change the is_admin field to true.
   *
   * @param id the id of the member
   * @return boolean
   */
  public MemberDTO isAdmin(int id) {
    String query = "UPDATE project_pae.members SET is_admin = true WHERE id_member = ? "
        + "RETURNING *;";
    return executeQueryWithId(id, query);
  }

  /**
   * Change the state of the member to denied.
   *
   * @param id the id of the member
   * @return boolean
   */
  public MemberDTO denyMember(int id) {
    String query = "UPDATE project_pae.members SET state = 'denied' WHERE id_member = ? "
        + "RETURNING *;";
    return executeQueryWithId(id, query);
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
        + "WHERE m.username = ?"
        + "  AND a.id_member = m.id_member";
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      System.out.println("Prepared statement successfully generated");
      preparedStatement.setString(1, username);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) { //We know only one is returned by the db
          System.out.println("USER FOUNDED");
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
   * Execute the query and return a list of member.
   *
   * @return a list of member
   */
  private List<MemberDTO> getMemberDTOS(List<MemberDTO> listMemberDTO, String query) {
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
   * Execute a query with an id param
   *
   * @param id    the id of the member
   * @param query the query to execute
   * @return boolean
   */
  private MemberDTO executeQueryWithId(int id, String query) {
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          MemberDTO memberDTO = createMemberInstance(rs);
          return memberDTO;
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
    memberDTO.setAddress(this.createAddressInstance(rs));
    return memberDTO;
  }

  // ************** DELETE ???

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
