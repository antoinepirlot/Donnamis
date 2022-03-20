package be.vinci.pae.dal.offer.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
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
  private DALServices dalServices;
  @Inject
  private Factory factory;


  @Override
  public boolean createOffer(OfferDTO offerDTO) {
    //Add first the item in the db
    if (!addItem(offerDTO.getItem())) {
      return false;
    }
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
    try {
      String query =
          "SELECT offers.id_offer, offers.date, offers.time_slot, items.id_item, "
              + "items.item_description, items.id_item_type, items.id_member, items.photo, "
              + "items.title, items.offer_status "
              + "FROM project_pae.offers offers "
              + "LEFT OUTER JOIN project_pae.items items ON offers.id_item = items.id_item "
              + "ORDER BY offers.date DESC;";
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          OfferDTO offerDTO = createOfferInstance(rs);
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
    try {
      String query =
          "SELECT offers.id_offer, offers.date, offers.time_slot, items.id_item, "
              + "items.item_description, items.id_member, items.photo, items.title, "
              + "items.offer_status, it.item_type, it.id_type "
              + "FROM project_pae.offers offers "
              + "LEFT OUTER JOIN project_pae.items items ON offers.id_item = items.id_item "
              + "LEFT OUTER JOIN project_pae.items_types it ON items.id_item_type = it.id_type;";
      PreparedStatement preparedStatement = dalServices.getPreparedStatement(query);
      System.out.println("Préparation du statement");
      try (ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
          OfferDTO offerDTO = createOfferInstance(rs);
          offersToReturn.add(offerDTO);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    System.out.println("Création des objets réussie");

    return offersToReturn;
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
      ps.setString(2, StringEscapeUtils.escapeHtml4(offerDTO.getTimeSlot()));
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

  /**
   * Add the item associated with the offer'id to the DB.
   *
   * @param itemDTO the item to add into the db
   * @return true if the item has been added, otherwise false
   */
  private boolean addItem(ItemDTO itemDTO) {
    String query = "INSERT INTO project_pae.items (item_description, id_item_type, id_member, "
        + "photo, title, offer_status) "
        + "VALUES (?, ?, ?, ?, ?, ?);";
    try (
        PreparedStatement ps = dalServices.getPreparedStatement(query)
    ) {
      ps.setString(1, StringEscapeUtils.escapeHtml4(itemDTO.getItemDescription()));
      ps.setInt(2, itemDTO.getItemType().getIdType());
      ps.setInt(3,  itemDTO.getMember().getId());
      ps.setString(4, StringEscapeUtils.escapeHtml4(itemDTO.getPhoto()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(itemDTO.getTitle()));
      ps.setString(6, StringEscapeUtils.escapeHtml4(itemDTO.getOfferStatus()));
      int result = ps.executeUpdate();
      if (result != 0) {
        System.out.println("Ajout de l'item réussi");
        return true;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  //****************************** UTILS *******************************

  /**
   * Create an Offer instance.
   *
   * @param rs the result set that contains sql result
   * @return a new instance of offer based on what rs contains
   * @throws SQLException if there's an issue while getting data from the result set
   */
  private OfferDTO createOfferInstance(ResultSet rs) throws SQLException {
    System.out.println("Offer instance creation");
    OfferDTO offerDTO = factory.getOffer();
    System.out.println("Factory réussie");
    offerDTO.setIdOffer(rs.getInt("id_offer"));
    System.out.println("id_offer set");
    System.out.println(rs.getDate("date"));
    offerDTO.setDate(rs.getDate("date"));
    System.out.println("date set");
    offerDTO.setTimeSlot(rs.getString("time_slot"));
    System.out.println("tile_slot set");
    offerDTO.setItem(ObjectsInstanceCreator.createItemInstance(this.factory, rs));

    return offerDTO;
  }
}
