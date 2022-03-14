package be.vinci.pae.dal;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.OfferDTO;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.text.StringEscapeUtils;

public class OfferDAOImpl implements OfferDAO {

  @Inject
  private DALServices dalServices;
  @Inject
  private Factory factory;

  @Override
  public boolean createOffer(OfferDTO offerDTO) {
    return addOne(offerDTO);
  }

  /**
   * Add the offer to the db.
   *
   * @param offerDTO the offer to add in the db
   * @return true if the offer has been added to the DB
   */
  private boolean addOne(OfferDTO offerDTO) {
    String query = "INSERT INTO project_pae.offers (date, time_slot, id_item) VALUES (?, ?, ?)";
    try (
        PreparedStatement ps = dalServices.getPreparedStatement(query)
    ) {
      ps.setDate(1, offerDTO.getDate());
      ps.setString(2, StringEscapeUtils.escapeHtml4(offerDTO.getTime_slot()));
      ps.setInt(3, offerDTO.getItem().getId());

      try {
        int result = ps.executeUpdate();
        //it adds into the db BUT can't execute getOne(), it returns null
        if (result != 0) {
          System.out.println("Ajout de l'offre réussie.");
          return true;
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

}
