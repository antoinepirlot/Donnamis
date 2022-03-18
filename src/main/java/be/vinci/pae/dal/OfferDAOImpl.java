package be.vinci.pae.dal;

import be.vinci.pae.biz.AddressDTO;
import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.ItemDTO;
import be.vinci.pae.biz.MemberDTO;
import be.vinci.pae.biz.OfferDTO;
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
          "SELECT offers.id_offer, offers.date, offers.time_slot, items.id_item, items.item_description, items.id_item_type, items.id_member, items.photo, items.title, items.offer_status\n"
              + "FROM project_pae.offers offers\n"
              + "         LEFT OUTER JOIN project_pae.items items ON offers.id_item = items.id_item\n"
              + "ORDER BY offers.date DESC ";
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
          "SELECT offers.id_offer, offers.date, offers.time_slot, items.id_item, items.item_description, items.id_item_type, items.id_member, items.photo, items.title, items.offer_status\n"
              + "FROM project_pae.offers offers\n"
              + "         LEFT OUTER JOIN project_pae.items items ON offers.id_item = items.id_item";
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
  public OfferDTO getOne(int id) {
    System.out.println("Get one offer by ID");
    String query =

        "SELECT item.id_item, item.id_item_type, item.photo, item.offer_status, item.title, item.id_member,"
        + "       item.item_description, item_type.item_type,offer.id_offer,offer.date, offer.time_slot,"
        + "       member.first_name, member.last_name"
        + " FROM project_pae.items item, project_pae.items_types item_type, project_pae.offers offer, project_pae.members member"
        + " WHERE item.id_item_type = item_type.id_type AND"
        + "       item.id_item = offer.id_item AND"
        + "       item.id_member = member.id_member AND"
        + "       offer.id_offer = ?";


    // "SELECT item.id_item, item.id_item_type, item.photo, item.offer_status,"
    //     + "item.title, item.id_member, item.item_description,item_type.item_type,"
    //     + "offer.id_offer, offer.date, offer.time_slot, member.first_name, member.last_name"
    //     + "FROM project_pae.items item, project_pae.items_types item_type,"
    //     + "project_pae.offers offer, project_pae.members member"
    //     + "WHERE item.id_item_type = item_type.id_type AND"
    //     + "item.id_item = offer.id_item AND"
    //     + "item.id_member = member.id_member AND"
    //     + "offer.id_offer = ?";
    try (PreparedStatement preparedStatement = dalServices.getPreparedStatement(query)) {
      System.out.println("Prepared statement successfully generated");
      preparedStatement.setInt(1, id);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) { //We know only one is returned by the db
          System.out.println("OFFER FOUNDED");
          return createOfferInstance(rs);
        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
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

  /**
   * Add the item associated with the offer'id to the DB
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
      ps.setString(1, StringEscapeUtils.escapeHtml4(itemDTO.getItem_description()));
      ps.setInt(2, itemDTO.getId_item_type());
      ps.setInt(3, itemDTO.getId_member());
      ps.setString(4, StringEscapeUtils.escapeHtml4(itemDTO.getPhoto()));
      ps.setString(5, StringEscapeUtils.escapeHtml4(itemDTO.getTitle()));
      ps.setString(6, StringEscapeUtils.escapeHtml4(itemDTO.getOffer_status()));
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
    offerDTO.setIdOffer(rs.getInt("id_offer"));
    offerDTO.setDate(rs.getDate("date"));
    offerDTO.setTime_slot(rs.getString("time_slot"));
    offerDTO.setItem(this.createItemInstance(rs));
    return offerDTO;
  }

  /**
   * Create an Item instance.
   *
   * @param rs the result set that contains sql result
   * @return a new instance of item based on what rs contains
   * @throws SQLException if there's an issue while getting data from the result set
   */
  private ItemDTO createItemInstance(ResultSet rs) throws SQLException {
    System.out.println("Item instance creation");
    ItemDTO itemDTO = factory.getItem();
    itemDTO.setId(rs.getInt("id_item"));
    itemDTO.setId_item_type(rs.getInt("id_item_type"));
    itemDTO.setId_member(rs.getInt("id_member"));
    itemDTO.setPhoto(rs.getString("photo"));
    itemDTO.setTitle(rs.getString("title"));
    itemDTO.setOffer_status(rs.getString("offer_status"));
    return itemDTO;
  }

//  /**
//   * Create a Member instance.
//   *
//   * @param rs the result set that contains sql result
//   * @return a new instance of member based on what rs contains
//   * @throws SQLException if there's an issue while getting data from the result set
//   */
//  private MemberDTO createMemberInstance(ResultSet rs) throws SQLException {
//    System.out.println("Member instance creation");
//    MemberDTO memberDTO = factory.getMember();
//    memberDTO.setId(rs.getInt("id_member"));
//    memberDTO.setUsername(rs.getString("username"));
//    memberDTO.setPassword(rs.getString("password"));
//    memberDTO.setLastName(rs.getString("last_name"));
//    memberDTO.setFirstName(rs.getString("first_name"));
//    memberDTO.setAdmin(rs.getBoolean("is_admin"));
//    memberDTO.setActualState(rs.getString("state"));
//    memberDTO.setPhoneNumber(rs.getString("phone"));
//    memberDTO.setAddress(this.createAddressInstance(rs));
//    return memberDTO;
//  }

//  /**
//   * Create an address instance.
//   *
//   * @param rs the result set that contains sql result
//   * @return a new instance of address based on what rs contains
//   * @throws SQLException if there's an issue while getting data from the result set
//   */
//  private AddressDTO createAddressInstance(ResultSet rs) throws SQLException {
//    System.out.println("Address instance creation");
//    AddressDTO addressDTO = factory.getAddress();
//    addressDTO.setId(rs.getInt("id_address"));
//    addressDTO.setStreet(rs.getString("street"));
//    addressDTO.setBuildingNumber(rs.getString("building_number"));
//    addressDTO.setUnitNumber(rs.getString("unit_number"));
//    addressDTO.setPostcode(rs.getString("postcode"));
//    addressDTO.setCommune(rs.getString("commune"));
//    addressDTO.setId(rs.getInt("id_member"));
//    return addressDTO;
//  }

}
