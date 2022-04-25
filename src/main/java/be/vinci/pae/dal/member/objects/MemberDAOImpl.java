package be.vinci.pae.dal.member.objects;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class MemberDAOImpl implements MemberDAO {

  private static final String DEFAULT_STATE = "registered";
  private static final String CONFIRMED_STATE = "confirmed";
  private static final String DENIED_STATE = "denied";
  private static final boolean DEFAULT_IS_ADMIN = false;
  @Inject
  private DALBackendService dalBackendService;
  @Inject
  private Factory factory;

  /**
   * Get all the members in the DB.
   *
   * @return all the members otherwise null
   */
  public List<MemberDTO> getAllMembers() throws SQLException {
    List<MemberDTO> listMemberDTO = new ArrayList<>();
    String query = "SELECT id_member, "
        + "                username, "
        + "                last_name, "
        + "                first_name, "
        + "                is_admin, "
        + "                state, "
        + "                phone "
        + "FROM project_pae.members";

    //Execute the query
    return getMembersDTO(listMemberDTO, query);
  }

  /**
   * Get a specific member identified by its username.
   *
   * @param memberDTO the member who try to log in
   * @return the member got from the db
   */
  @Override
  public MemberDTO getOne(MemberDTO memberDTO) throws SQLException {
    String query = "SELECT m.id_member, m.username, m.password, m.last_name, m.first_name, "
        + "m.is_admin, m.state, m.phone, a.id_address, a.street, a.building_number, a.unit_number,"
        + "a.postcode, a.commune "
        + "FROM project_pae.members m, project_pae.addresses a "
        + "WHERE a.id_member = m.id_member ";
    if (memberDTO.getUsername() != null) {
      query += " AND m.username = ?;";
    } else {
      query += " AND m.id_member = ?;";
    }
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      if (memberDTO.getUsername() != null) {
        preparedStatement.setString(1, StringEscapeUtils
            .escapeHtml4(memberDTO.getUsername()));
      } else {
        preparedStatement.setInt(1, memberDTO.getId());
      }
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) { //We know only one is returned by the db
          return ObjectsInstanceCreator.createMemberInstance(this.factory, rs);
        } else {
          return null;
        }
      }
    }
  }

  @Override
  public MemberDTO getOne(int id) throws SQLException {
    MemberDTO memberDTO = this.factory.getMember();
    memberDTO.setId(id);
    return this.getOne(memberDTO);
  }

  @Override
  public MemberDTO modifyMember(MemberDTO memberDTO) throws SQLException {
    String query = "UPDATE project_pae.members SET username = ?, password = ?, last_name = ?, "
        + "first_name = ?, phone = ? WHERE id_member = ? RETURNING *;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setString(1, memberDTO.getUsername());
      preparedStatement.setString(2, memberDTO.getPassword());
      preparedStatement.setString(3, memberDTO.getLastName());
      preparedStatement.setString(4, memberDTO.getFirstName());
      preparedStatement.setString(5, memberDTO.getPhoneNumber());
      preparedStatement.setInt(6, memberDTO.getId());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return ObjectsInstanceCreator.createMemberInstance(this.factory, rs);
        } else {
          return null;
        }
      }
    }
  }

  /**
   * Change the state of the member to confirmed.
   *
   * @param memberDTO the member to confirm
   * @return boolean
   */
  public boolean confirmMember(MemberDTO memberDTO) throws SQLException {
    String query = "UPDATE project_pae.members "
        + "SET state = '" + CONFIRMED_STATE + "', is_admin = ? "
        + "WHERE id_member = ?;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setBoolean(1, memberDTO.isAdmin());
      preparedStatement.setInt(2, memberDTO.getId());
      return preparedStatement.executeUpdate() != 0;
    }
  }

  /**
   * Change the is_admin field to true.
   *
   * @param id the id of the member
   * @return boolean
   */
  public MemberDTO isAdmin(int id) throws SQLException {
    String query = "UPDATE project_pae.members SET is_admin = true WHERE id_member = ? "
        + "RETURNING *;";
    return executeQueryWithId(id, query);
  }

  @Override
  public boolean denyMember(RefusalDTO refusalDTO) throws SQLException {
    String query =
        "UPDATE project_pae.members SET state = '" + DENIED_STATE + "' WHERE id_member = ?;"
            + "INSERT INTO project_pae.refusals (text, id_member) VALUES (?, ?);";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, refusalDTO.getMember().getId());
      preparedStatement.setString(2,
          StringEscapeUtils.escapeHtml4(refusalDTO.getText())
      );
      preparedStatement.setInt(3, refusalDTO.getMember().getId());
      return preparedStatement.executeUpdate() != 0;
    }
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO, int idMember) throws SQLException {
    String query = "SELECT id_member FROM project_pae.members WHERE id_member = ?";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      if (memberDTO == null) {
        preparedStatement.setInt(1, idMember);
      } else {
        preparedStatement.setInt(1, memberDTO.getId());
      }
      try (ResultSet rs = preparedStatement.executeQuery()) {
        return rs.next();
      }
    }
  }

  @Override
  public List<MemberDTO> getInterestedMembers(int idOffer) throws SQLException {
    String query = "SELECT  m2.id_member, "
        + "        m2.username, "
        + "        m2.last_name, "
        + "        m2.first_name, "
        + "        m2.is_admin, "
        + "        m2.state, "
        + "        m2.phone "
        + "FROM project_pae.members m, "
        + "     project_pae.members m2, "
        + "     project_pae.interests int, "
        + "     project_pae.items i, "
        + "     project_pae.offers o "
        + "WHERE i.id_member = m.id_member "
        + "  AND m2.id_member = int.id_member "
        + "  AND int.id_offer = o.id_offer "
        + "  AND o.id_item = i.id_item "
        + "  AND o.id_offer = ?;";
    List<MemberDTO> memberDTOList = new ArrayList<>();
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idOffer);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          memberDTOList.add(ObjectsInstanceCreator.createMemberInstance(this.factory, rs));
        }
      }
    }
    return memberDTOList.isEmpty() ? null : memberDTOList;
  }

  //****************************** UTILS *******************************

  /**
   * Execute the query and return a list of member.
   *
   * @return a list of member
   */
  private List<MemberDTO> getMembersDTO(List<MemberDTO> listMemberDTO, String query)
      throws SQLException {
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          listMemberDTO.add(ObjectsInstanceCreator.createMemberInstance(this.factory, rs));
        }
        return listMemberDTO.isEmpty() ? null : listMemberDTO;
      }
    }
  }

  /**
   * Execute a query with an id param.
   *
   * @param id    the id of the member
   * @param query the query to execute
   * @return boolean
   */
  private MemberDTO executeQueryWithId(int id, String query) throws SQLException {
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return ObjectsInstanceCreator.createMemberInstance(this.factory, rs);
        } else {
          return null;
        }
      }
    }
  }

  /**
   * Add a new member to the db if it's not already in the db, then its address.
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been  registered
   */
  public boolean register(MemberDTO memberDTO) throws SQLException {
    MemberDTO memberDB = this.getOne(memberDTO);
    if (memberDB != null) { // the user already exists !
      return false;
    }
    //Add the member to the db then the address
    int idMember = addOne(memberDTO);
    if (idMember == -1) {
      return false;
    }
    return addAddress(memberDTO.getId(), memberDTO.getAddress());
  }

  /**
   * Add the member to the db.
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been added to the DB
   */
  private int addOne(MemberDTO memberDTO) throws SQLException {
    String query = "INSERT INTO project_pae.members (username, password, last_name, first_name, "
        + "is_admin, state) VALUES (?, ?, ?, ?, ?, ?)"
        + "RETURNING id_member;";
    try (
        PreparedStatement ps = dalBackendService.getPreparedStatement(query)
    ) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(memberDTO.getUsername()));
      ps.setString(2, memberDTO.getPassword());
      ps.setString(3, StringEscapeUtils.escapeHtml4(memberDTO.getLastName()));
      ps.setString(4, StringEscapeUtils.escapeHtml4(memberDTO.getFirstName()));
      ps.setBoolean(5, DEFAULT_IS_ADMIN);
      ps.setString(6, DEFAULT_STATE);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        } else {
          return -1;
        }
      }
    }
  }

  /**
   * Add the address associated with the member's id to the DB.
   *
   * @param addressDTO the address to add into the db
   * @return true if the address has been added, otherwise false
   */
  private boolean addAddress(int memberId, AddressDTO addressDTO) throws SQLException {
    String query = "INSERT INTO project_pae.addresses (street, building_number, unit_number, "
        + "postcode, commune, id_member) "
        + "VALUES (?, ?, ?, ?, ?, ?);";
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(addressDTO.getStreet()));
      ps.setString(2, StringEscapeUtils.escapeHtml4(addressDTO.getBuildingNumber()));
      ps.setString(3, StringEscapeUtils.escapeHtml4(addressDTO.getUnitNumber()));
      ps.setString(4, StringEscapeUtils.escapeHtml4(addressDTO.getPostcode()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(addressDTO.getCommune()));
      ps.setInt(6, memberId);
      int result = ps.executeUpdate();
      return result != 0;
    }
  }
}
