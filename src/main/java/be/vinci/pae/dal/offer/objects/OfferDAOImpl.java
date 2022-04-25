package be.vinci.pae.dal.offer.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.utils.ObjectsInstanceCreator;
import jakarta.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class OfferDAOImpl implements OfferDAO {

  private static final String DEFAULT_OFFER_STATUS = "donated";
  @Inject
  private DALBackendService dalBackendService;
  @Inject
  private Factory factory;

  @Override
  public boolean createOffer(OfferDTO offerDTO) throws SQLException {
    //Add first the item in the db
    //    if (!addItem(offerDTO)) {
    //      return false;
    //    }
    //If the item is correctly added , add the offer in the db with the associated item
    return addOne(offerDTO);
  }

  @Override
  public List<OfferDTO> getAllOffers(String offerStatus) throws SQLException {
    List<OfferDTO> offersToReturn = new ArrayList<>();
    String query = "SELECT o.id_offer, "
        + "                o.date, "
        + "                o.time_slot, "
        + "                o.id_item "
        + "FROM project_pae.offers o ";
    if (offerStatus != null) {
      query += ", project_pae.items i "
          + "WHERE o.id_item = i.id_item "
          + "  AND i.offer_status = ? ";
    }
    query += "ORDER BY date DESC;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      if (offerStatus != null) {
        preparedStatement.setString(1, StringEscapeUtils.escapeHtml4(offerStatus));
      }
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          OfferDTO offerDTO = ObjectsInstanceCreator.createOfferInstance(this.factory, rs);
          offersToReturn.add(offerDTO);
        }
        return offersToReturn.isEmpty() ? null : offersToReturn;
      }
    }
  }

  @Override
  public OfferDTO getOne(int id) throws SQLException {
    String query =

        "SELECT item.id_item, item.photo, item.offer_status, item.title, "
            + "item.id_member, item.item_description, "
            + "item_type.item_type, item_type.id_type, "
            + "offer.id_offer,offer.date, offer.time_slot,"
            + "member.first_name, member.last_name, member.username "
            + "FROM project_pae.items item, project_pae.items_types item_type, "
            + "     project_pae.offers offer, project_pae.members member "
            + "WHERE item.id_type = item_type.id_type "
            + "  AND item.id_item = offer.id_item "
            + "  AND item.id_member = member.id_member "
            + "  AND offer.id_offer = ?;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          return ObjectsInstanceCreator.createOfferInstance(this.factory, rs);
        }
        return null;
      }
    }
  }

  @Override
  public boolean offerExist(OfferDTO offerDTO) throws SQLException {
    String query = "SELECT id_offer FROM project_pae.offers WHERE id_offer = ?";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, offerDTO.getId());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        return rs.next();
      }
    }
  }

  @Override
  public OfferDTO getLastOfferOf(ItemDTO itemDTO) throws SQLException {
    String query = "SELECT id_offer, date, time_slot, id_item "
        + "FROM project_pae.offers o "
        + "WHERE id_item = ? "
        + "ORDER BY date DESC "
        + "LIMIT 1;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, itemDTO.getId());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) { //We know only one is returned by the db
          return ObjectsInstanceCreator.createOfferInstance(this.factory, rs);
        }
        return null;
      }
    }
  }


  /**
   * Add the offer to the db.
   *
   * @param offerDTO the offer to add in the db
   * @return true if the offer has been added to the DB
   */
  private boolean addOne(OfferDTO offerDTO) throws SQLException {
    String query = "INSERT INTO project_pae.offers (date, time_slot, id_item) "
        + "VALUES (?, ?, ?); "
        + "UPDATE project_pae.items SET offer_status = '" + DEFAULT_OFFER_STATUS + "', "
        + "last_offer_date  = ? "
        + "WHERE id_item = ?";
    System.out.println(query);
    try (
        PreparedStatement ps = dalBackendService.getPreparedStatement(query)
    ) {
      ps.setTimestamp(1, offerDTO.getDate());
      ps.setString(2, StringEscapeUtils.escapeHtml4(offerDTO.getTimeSlot()));
      ps.setInt(3, offerDTO.getIdItem());
      ps.setTimestamp(4, offerDTO.getDate());
      ps.setInt(5, offerDTO.getIdItem());
      return ps.executeUpdate() != 0;
    }
  }

  //  /**
  //   * Add the item associated with the offer'id to the DB.
  //   *
  //   * @param itemDTO the item to add into the db
  //   * @return true if the item has been added, otherwise false
  //   */
  //  private boolean addItem(ItemDTO itemDTO) {
  //    String query = "INSERT INTO project_pae.items (item_description, id_type, id_member, "
  //        + "photo, title, offer_status) "
  //        + "VALUES (?, ?, ?, ?, ?, ?);";
  //    try (
  //        PreparedStatement ps = dalBackendService.getPreparedStatement(query)
  //    ) {
  //      ps.setString(1, StringEscapeUtils.escapeHtml4(itemDTO.getItemDescription()));
  //      ps.setInt(2, itemDTO.getItemType().getId());
  //      ps.setInt(3, itemDTO.getMember().getId());
  //      ps.setString(4, StringEscapeUtils.escapeHtml4(itemDTO.getPhoto()));
  //      ps.setString(5, StringEscapeUtils.escapeHtml4(itemDTO.getTitle()));
  //      ps.setString(6, StringEscapeUtils.escapeHtml4(itemDTO.getOfferStatus()));
  //      int result = ps.executeUpdate();
  //      if (result != 0) {
  //        System.out.println("Ajout de l'item réussi");
  //        return true;
  //      }
  //    } catch (SQLException e) {
  //      e.printStackTrace();
  //    }
  //    return false;
  //  }

  //****************************** UTILS *******************************

}
