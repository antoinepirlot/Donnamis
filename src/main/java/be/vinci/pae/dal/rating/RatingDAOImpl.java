package be.vinci.pae.dal.rating;

import be.vinci.pae.biz.rating.RatingDTO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingDAOImpl implements RatingDAO {

  @Inject
  private DALBackendService dalBackendService;

  @Override
  public boolean ratingExist(RatingDTO ratingDTO) throws SQLException {
    String query = "SELECT * FROM project_pae.ratings WHERE id_item = ? AND id_member = ?;";

    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, ratingDTO.getItem().getId());
      ps.setInt(2, ratingDTO.getMember().getId());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean evaluate(RatingDTO ratingDTO) throws SQLException {
    String query = "INSERT INTO project_pae.ratings (id_item, rating, text, id_member) VALUES"
        + "(?, ?, ?, ?);";

    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, ratingDTO.getItem().getId());
      ps.setInt(2, ratingDTO.getRating());
      ps.setString(3, ratingDTO.getText());
      ps.setInt(4, ratingDTO.getMember().getId());

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return true;
        }
      }
    }
    return false;
  }
}
