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
    if (!addOne(offerDTO)) {
      return false;
    }
    //Add item to the offer

    return addOne(offerDTO);
  }

  @Override
  public List<OfferDTO> getLatestOffers() {
    System.out.println("getLatestOffers");
    List<OfferDTO> offersToReturn = new ArrayList<>();
    String query =
        "SELECT offers.id_offer,\n"
            + "       offers.date,\n"
            + "       offers.time_slot,\n"
            + "       items.id_item,\n"
            + "       items.item_description,\n"
            + "       items.id_type,\n"
            + "       items.id_member,\n"
            + "       items.photo,\n"
            + "       items.title,\n"
            + "       items.offer_status,\n"
            + "       it.item_type\n"
            + "FROM project_pae.offers offers\n"
            + "         LEFT OUTER JOIN project_pae.items items ON offers.id_item = items.id_item\n"
            + "         LEFT OUTER JOIN project_pae.items_types it ON it.id_type = items.id_type\n"
            + "WHERE items.offer_status = 'donated'\n"
            + "ORDER BY offers.date DESC;";
    System.out.println("Préparation du statement");
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      if (preparedStatement == null) {
        throw new NullPointerException();
      }
      try (ResultSet rs = preparedStatement.executeQuery()) {

        while (rs.next()) {
          OfferDTO offerDTO = ObjectsInstanceCreator.createOfferInstance(this.factory, rs);
          offersToReturn.add(offerDTO);
        }
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Création des objets réussie");

    return offersToReturn;
  }

  @Override
  public List<OfferDTO> getAllOffers() {
    System.out.println("getAllOffers");
    List<OfferDTO> offersToReturn = new ArrayList<>();
    String query =
        "SELECT offers.id_offer, offers.date, offers.time_slot, items.id_item, "
            + "items.item_description, items.id_type, items.id_member, items.photo, "
            + "items.title, items.offer_status "
            + "FROM project_pae.offers offers "
            + "LEFT OUTER JOIN project_pae.items items ON offers.id_item = items.id_item;";
    System.out.println("Préparation du statement");
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          OfferDTO offerDTO = ObjectsInstanceCreator.createOfferInstance(this.factory, rs);
          offersToReturn.add(offerDTO);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Création des objets réussie");

    return offersToReturn;
  }

  @Override
  public OfferDTO getOne(int id) {
    System.out.println("Get one offer by ID");
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
      System.out.println("Prepared statement successfully generated");
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) { //We know only one is returned by the db
          System.out.println("OFFER FOUNDED");
          return ObjectsInstanceCreator.createOfferInstance(this.factory, rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public OfferDTO getLastOfferOf(ItemDTO itemDTO) throws SQLException {
    System.out.println("Get latest item's offer");
    String query = "SELECT id_offer, date, time_slot, id_item "
        + "FROM project_pae.offers o "
        + "WHERE id_item = ? "
        + "ORDER BY date DESC "
        + "LIMIT 1;";
    try (PreparedStatement preparedStatement = dalBackendService.getPreparedStatement(query)) {
      System.out.println("Prepared statement successfully generated");
      preparedStatement.setInt(1, itemDTO.getId());
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) { //We know only one is returned by the db
          System.out.println("OFFER FOUNDED");
          return ObjectsInstanceCreator.createOfferInstance(this.factory, rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      throw e;
    }
    return null;
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
        PreparedStatement ps = dalBackendService.getPreparedStatement(query)
    ) {
      ps.setDate(1, offerDTO.getDate());
      ps.setString(2, StringEscapeUtils.escapeHtml4(offerDTO.getTimeSlot()));
      ps.setInt(3, offerDTO.getIdItem());

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
