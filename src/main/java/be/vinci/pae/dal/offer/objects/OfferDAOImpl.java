package be.vinci.pae.dal.offer.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
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

public class OfferDAOImpl implements OfferDAO {

  private static final String DEFAULT_OFFER_STATUS = "donated";
  @Inject
  private DALBackendService dalBackendService;
  @Inject
  private Factory factory;

  @Override
  public boolean createOffer(OfferDTO offerDTO) {
    //Add first the item in the db
    //    if (!addItem(offerDTO)) {
    //      return false;
    //    }
    //If the item is correctly added , add the offer in the db with the associated item
    try {
      return addOne(offerDTO);
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<OfferDTO> getAllOffers(String offerStatus) {
    List<OfferDTO> offersToReturn = new ArrayList<>();
    String query = "SELECT o.id_offer, "
        + "                o.date, "
        + "                o.time_slot, "
        + "                o.id_item, "
        + "                o.number_of_interests, "
        + "                o.version_offer "
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
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public OfferDTO getOne(int id) {
    String query = "SELECT i.id_item, "
        + "                i.photo, "
        + "                i.offer_status, "
        + "                i.title, "
        + "                i.id_member, "
        + "                i.item_description,  "
        + "                i.version_item, "
        + "                it.item_type,"
        + "                it.id_type, "
        + "                it.version_items_type, "
        + "                o.id_offer, "
        + "                o.date, "
        + "                o.time_slot, "
        + "                o.number_of_interests, "
        + "                o.version_offer, "
        + "                m.first_name, "
        + "                m.last_name, "
        + "                m.username,"
        + "                m.version_member "
        + "FROM project_pae.items i, "
        + "     project_pae.items_types it, "
        + "     project_pae.offers o, "
        + "     project_pae.members m "
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
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public boolean offerExist(OfferDTO offerDTO) {
    String query = "SELECT id_offer FROM project_pae.offers WHERE id_offer = ?";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, offerDTO.getId());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public List<OfferDTO> getLastTwoOffersOf(ItemDTO itemDTO) {
    String query = "SELECT id_offer, date, time_slot, id_item, number_of_interests, version_offer "
        + "FROM project_pae.offers "
        + "WHERE id_item = ? "
        + "ORDER BY date DESC "
        + "LIMIT 2;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      preparedStatement.setInt(1, itemDTO.getId());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        List<OfferDTO> offerDTOList = new ArrayList<>();
        while (rs.next()) { //We know only one is returned by the db
          offerDTOList.add(ObjectsInstanceCreator.createOfferInstance(this.factory, rs));
        }
        return offerDTOList.isEmpty() ? null : offerDTOList;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  @Override
  public int getNumberOfInterestedMemberOf(int idItem) {
    String getLastOfferId = "(SELECT id_offer "
        + "FROM project_pae.offers "
        + "WHERE id_item = ? "
        + "ORDER BY date DESC "
        + "LIMIT 1)";
    String query = "SELECT number_of_interests "
        + "FROM project_pae.offers "
        + "WHERE id_offer = " + getLastOfferId + "";
    try (PreparedStatement ps = this.dalBackendService.getPreparedStatement(query)) {
      ps.setInt(1, idItem);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? rs.getInt(1) : -1;
      }
    } catch (SQLException e) {
      throw new FatalException(e);
    }
  }

  //****************************** UTILS *******************************

  /**
   * Add the offer to the db.
   *
   * @param offerDTO the offer to add in the db
   * @return true if the offer has been added to the DB
   */
  private boolean addOne(OfferDTO offerDTO) throws SQLException {
    String query = "INSERT INTO project_pae.offers (date, time_slot, id_item, number_of_interests, "
        + "version_offer) "
        + "VALUES (?, ?, ?, 0, 1); "
        + "UPDATE project_pae.items SET offer_status = '" + DEFAULT_OFFER_STATUS + "', "
        + "last_offer_date  = ?, version_item = version_item + 1 "
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

}
