package be.vinci.pae.dal.interest;

import be.vinci.pae.dal.DALServices;
import jakarta.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InterestDAOImpl implements InterestDAO {

  @Inject
  private DALServices dalServices;
  //@Inject
  //private Factory factory;

  @Override
  public int markInterest(int id_member, int id_offer, boolean call_wanted, LocalDate date) {
    String query =
        "INSERT INTO project_pae.interests (call_wanted, id_offer, id_member, date) VALUES"
            + "(?, ?, ?, ?) RETURNING *";
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      preparedStatement.setBoolean(1, call_wanted);
      preparedStatement.setInt(2, id_offer);
      preparedStatement.setInt(3, id_member);
      preparedStatement.setDate(4, Date.valueOf(date));
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
  public boolean offerExist(int id_offer) {
    String query = "SELECT * FROM project_pae.offers WHERE id_offer = ?";
    return executeQueryWithId(id_offer, query);
  }

  @Override
  public boolean memberExist(int id_member) {
    String query = "SELECT * FROM project_pae.members WHERE id_member = ?";
    return executeQueryWithId(id_member, query);
  }

  @Override
  public boolean interestExist(int id_offer, int id_member) {
    String query = "SELECT * FROM project_pae.interests WHERE id_offer = ? AND id_member = ?";
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id_offer);
      preparedStatement.setInt(2, id_member);
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

  private boolean executeQueryWithId(int id_member, String query) {
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id_member);
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
