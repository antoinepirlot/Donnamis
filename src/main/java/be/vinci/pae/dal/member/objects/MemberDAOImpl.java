package be.vinci.pae.dal.member.objects;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import be.vinci.pae.exceptions.FatalException;
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
  public List<MemberDTO> getAllMembers() {
    List<MemberDTO> listMemberDTO = new ArrayList<>();
    String query = "SELECT m.id_member, "
        + "                m.username, "
        + "                m.last_name, "
        + "                m.first_name, "
        + "                m.is_admin, "
        + "                m.state, "
        + "                m.phone, "
        + "                m.version_member, "
        + "                a.id_address, "
        + "                a.street, "
        + "                a.building_number, "
        + "                a.unit_number, "
        + "                a.postcode, "
        + "                a.commune,"
        + "                a.version_address "
        + "FROM project_pae.members m, "
        + "     project_pae.addresses a "
        + "WHERE m.id_member = a.id_member;";

    //Execute the query
    try {
      return getMembersDTO(listMemberDTO, query);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Get a specific member identified by its username.
   *
   * @param memberDTO the member who try to log in
   * @return the member got from the db
   */
  @Override
  public MemberDTO getOne(MemberDTO memberDTO) {
    String query = "SELECT m.id_member, "
        + "                m.username, "
        + "                m.password, "
        + "                m.last_name, "
        + "                m.first_name, "
        + "                m.is_admin, "
        + "                m.state, "
        + "                m.phone, "
        + "                m.version_member, "
        + "                a.id_address, "
        + "                a.street, "
        + "                a.building_number, "
        + "                a.unit_number,"
        + "                a.postcode, "
        + "                a.commune, "
        + "                a.version_address "
        + "FROM project_pae.members m, "
        + "     project_pae.addresses a "
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
        }
        return null;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public MemberDTO getOne(int id) {
    MemberDTO memberDTO = this.factory.getMember();
    memberDTO.setId(id);
    return this.getOne(memberDTO);
  }

  @Override
  public boolean modifyMember(MemberDTO memberDTO) {
    String query = "UPDATE project_pae.members "
        + "SET username = ?, "
        + "password = ?, "
        + "last_name = ?, "
        + "first_name = ?, "
        + "phone = ?, "
        + "version_member = version_member + 1 "
        + "WHERE id_member = ? ; "
        + "UPDATE project_pae.addresses "
        + "SET street = ?, "
        + "building_number = ?, ";
    if (memberDTO.getAddress().getUnitNumber() != null) {
      query += "unit_number = ?, ";
    }
    query += "postcode = ?, "
        + "commune = ?, "
        + "version_address = version_address+1 "
        + "WHERE id_member = ?;";
    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setString(1, memberDTO.getUsername());
      ps.setString(2, memberDTO.getPassword());
      ps.setString(3, memberDTO.getLastName());
      ps.setString(4, memberDTO.getFirstName());
      ps.setString(5, memberDTO.getPhoneNumber());
      ps.setInt(6, memberDTO.getId());
      ps.setString(7, StringEscapeUtils.escapeHtml4(memberDTO.getAddress().getStreet()));
      ps.setString(8, StringEscapeUtils.escapeHtml4(memberDTO.getAddress().getBuildingNumber()));
      if (memberDTO.getAddress().getUnitNumber() != null) {
        ps.setString(9, StringEscapeUtils.escapeHtml4(memberDTO.getAddress().getUnitNumber()));
        ps.setString(10, StringEscapeUtils.escapeHtml4(memberDTO.getAddress().getPostcode()));
        ps.setString(11, StringEscapeUtils.escapeHtml4(memberDTO.getAddress().getCommune()));
        ps.setInt(12, memberDTO.getId());
      } else {
        ps.setString(9, StringEscapeUtils.escapeHtml4(memberDTO.getAddress().getPostcode()));
        ps.setString(10, StringEscapeUtils.escapeHtml4(memberDTO.getAddress().getCommune()));
        ps.setInt(11, memberDTO.getId());
      }
      return ps.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Confirm the member.
   *
   * @param memberDTO the member to confirm
   * @return true if the member has been confirmed, otherwise false
   */
  public boolean confirmMember(MemberDTO memberDTO) {
    String query = "UPDATE project_pae.members "
        + "SET state = '" + CONFIRMED_STATE
        + "', is_admin = ?, version_member = version_member + 1 "
        + "WHERE id_member = ?;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setBoolean(1, memberDTO.isAdmin());
      preparedStatement.setInt(2, memberDTO.getId());
      return preparedStatement.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Switch the state of the member between confirmed and unavailable.
   *
   * @param memberDTO the member to modify state
   * @return true if the member's state is change, otherwise false
   */
  public boolean setMemberAvailability(MemberDTO memberDTO) {
    String query = "UPDATE project_pae.members SET state = ?, "
        + "version_member = version_member + 1 WHERE id_member = ?;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setString(1, memberDTO.getActualState());
      preparedStatement.setInt(2, memberDTO.getId());
      return preparedStatement.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean denyMember(RefusalDTO refusalDTO) {
    String query = "UPDATE project_pae.members "
        + "SET state = '" + DENIED_STATE + "', "
        + "        version_member = version_member + 1 "
        + "WHERE id_member = ?; "
        + "INSERT INTO project_pae.refusals (text, id_member, version_refusal) "
        + "VALUES (?, ?, 1);";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, refusalDTO.getMember().getId());
      preparedStatement.setString(2,
          StringEscapeUtils.escapeHtml4(refusalDTO.getText())
      );
      preparedStatement.setInt(3, refusalDTO.getMember().getId());
      return preparedStatement.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO, int idMember) {
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
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<MemberDTO> getInterestedMembers(int idOffer) {
    String query = "SELECT  m2.id_member, "
        + "        m2.username, "
        + "        m2.last_name, "
        + "        m2.first_name, "
        + "        m2.is_admin, "
        + "        m2.state, "
        + "        m2.phone, "
        + "        m2.version_member "
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
        return memberDTOList.isEmpty() ? null : memberDTOList;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
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
   * Add a new member to the db if it's not already in the db, then its address.
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been  registered
   */
  public boolean register(MemberDTO memberDTO) {
    MemberDTO memberDB = this.getOne(memberDTO);
    if (memberDB != null) { // the user already exists !
      return false;
    }
    try {
      //Add the member to the db then the address
      int idMember = addOne(memberDTO);
      if (idMember == -1) {
        return false;
      }
      return addAddress(idMember, memberDTO.getAddress());
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Add the member to the db.
   *
   * @param memberDTO the member to add in the db
   * @return true if the member has been added to the DB
   */
  private int addOne(MemberDTO memberDTO) throws SQLException {
    String query = "INSERT INTO project_pae.members (username, password, last_name, first_name, "
        + "is_admin, state, version_member) VALUES (?, ?, ?, ?, ?, ?, 1)"
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
        + "postcode, commune, id_member, version_address) "
        + "VALUES (?, ?, ?, ?, ?, ?, 1);";
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
