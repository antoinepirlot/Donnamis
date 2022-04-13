package be.vinci.pae.dal.interest.objects;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class InterestDAOImpl implements InterestDAO {

  @Inject
  private DALBackendService dalBackendService;
  //@Inject
  //private Factory factory;

  @Override
  public int markInterest(MemberDTO memberDTO, int idItem, boolean callWanted, Timestamp date) {
    String query =
        "INSERT INTO project_pae.interests (call_wanted, id_item, id_member, date) VALUES"
            + "(?, ?, ?, ?) RETURNING *";

    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setBoolean(1, callWanted);
      preparedStatement.setInt(2, idItem);
      preparedStatement.setInt(3, memberDTO.getId());
      preparedStatement.setTimestamp(4, date);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return 1;
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return -1;
  }

  @Override
  public boolean offerExist(int idOffer) {
    String query = "SELECT * FROM project_pae.offers WHERE id_offer = ?";
    return executeQueryWithId(idOffer, query);
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO) {
    String query = "SELECT * FROM project_pae.members WHERE id_member = ?";
    return executeQueryWithId(memberDTO.getId(), query);
  }

  @Override
  public boolean interestExist(int idItem, MemberDTO memberDTO) {
    String query = "SELECT * FROM project_pae.interests WHERE id_item = ? AND id_member = ?";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, idItem);
      preparedStatement.setInt(2, memberDTO.getId());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return true;
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  //*****UTILS******

  private boolean executeQueryWithId(int id, String query) {
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return true;
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }
}
