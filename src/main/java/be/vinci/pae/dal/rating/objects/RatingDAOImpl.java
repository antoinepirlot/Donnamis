package be.vinci.pae.dal.rating.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.dal.rating.interfaces.RatingDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import be.vinci.pae.exceptions.FatalException;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RatingDAOImpl implements RatingDAO {

  @Inject
  private DALBackendService dalBackendService;

  @Inject
  private Factory factory;

  @Override
  public boolean ratingExist(RatingDTO ratingDTO) {
    String query = "SELECT id_rating FROM project_pae.ratings WHERE id_item = ? AND id_member = ?;";

    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, ratingDTO.getItem().getId());
      ps.setInt(2, ratingDTO.getMember().getId());
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean evaluate(RatingDTO ratingDTO) {
    String query = "INSERT INTO project_pae.ratings (id_item, rating, text, id_member, "
        + "version_rating) "
        + "VALUES (?, ?, ?, ?, 1);";

    try (PreparedStatement ps = dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, ratingDTO.getItem().getId());
      ps.setInt(2, ratingDTO.getRating());
      ps.setString(3, ratingDTO.getText());
      ps.setInt(4, ratingDTO.getMember().getId());
      return ps.executeUpdate() != 0;
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<RatingDTO> getAllRatingsOfMember(int idMember) {
    String query = "SELECT r.id_rating, "
        + "                r.rating, "
        + "                r.id_item, "
        + "                r.text, "
        + "                r.id_member,"
        + "                r.version_rating,"
        + "                i.id_item, "
        + "                i.item_description, "
        + "                i.id_type, "
        + "                i.id_member, "
        + "                i.photo, "
        + "                i.title, "
        + "                i.offer_status, "
        + "                i.version_item "
        + "FROM project_pae.ratings r, "
        + "     project_pae.items i "
        + "WHERE r.id_item = i.id_item "
        + "  AND r.id_member = ?;";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idMember);
      try (ResultSet rs = ps.executeQuery()) {
        List<RatingDTO> ratingDTOList = new ArrayList<>();
        while (rs.next()) {
          ratingDTOList.add(ObjectsInstanceCreator.createRatingInstance(this.factory, rs));
        }
        return ratingDTOList.isEmpty() ? null : ratingDTOList;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }
}
