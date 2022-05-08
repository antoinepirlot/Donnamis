package be.vinci.pae.dal.utils;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.rating.interfaces.RatingDTO;
import be.vinci.pae.biz.refusal.interfaces.RefusalDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.text.StringEscapeUtils;

public class ObjectsInstanceCreator {

  /**
   * Create an Offer instance with the factory and set all its attributes * with data selected from
   * the db.
   *
   * @param factory the factory that gives an item
   * @param rs      the result set that contains sql result
   * @return a new instance of offer based on what rs contains
   * @throws SQLException if there's an issue while getting data from the result set
   */
  public static OfferDTO createOfferInstance(Factory factory, ResultSet rs) throws SQLException {
    //Create Offer Instance
    OfferDTO offerDTO = factory.getOffer();

    //Set Attributes
    offerDTO.setId(rs.getInt("id_offer"));
    offerDTO.setDate(rs.getTimestamp("date"));
    offerDTO.setTimeSlot(rs.getString("time_slot"));
    offerDTO.setIdItem(rs.getInt("id_item"));
    offerDTO.setNumberOfInterests(rs.getInt("number_of_interests"));
    offerDTO.setVersion(rs.getInt("version_offer"));
    return offerDTO;
  }

  /**
   * Create an item instance with the factory and set all its attributes with data selected from the
   * db.
   *
   * @param factory the factory that gives an item
   * @param rs      the result set that contains item's data
   * @return a new item instance with initialized attributes
   */
  public static ItemDTO createItemInstance(Factory factory, ResultSet rs) throws SQLException {
    //Create Item Instance
    ItemDTO itemDTO = factory.getItem();

    //Set Attributes
    itemDTO.setId(rs.getInt("id_item"));
    try {
      itemDTO.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("title")));
    } catch (SQLException e) {
      itemDTO.setTitle(null);
    }
    try {
      itemDTO.setPhoto(StringEscapeUtils.escapeHtml4(rs.getString("photo")));
    } catch (SQLException e) {
      itemDTO.setPhoto(null);
    }
    try {
      OfferDTO offerDTO = factory.getOffer();
      offerDTO.setDate(rs.getTimestamp("last_offer_date"));
      itemDTO.setLastOffer(offerDTO);
    } catch (SQLException e) {
      itemDTO.setLastOffer(null);
    }
    try {
      itemDTO.setItemDescription(
          StringEscapeUtils.escapeHtml4(rs.getString("item_description"))
      );
    } catch (SQLException e) {
      itemDTO.setItemDescription(null);
    }
    try {
      itemDTO.setOfferStatus(
          StringEscapeUtils.escapeHtml4(rs.getString("offer_status"))
      );
    } catch (SQLException e) {
      itemDTO.setOfferStatus(null);
    }
    try {
      itemDTO.setItemType(createItemsTypeInstance(factory, rs));
    } catch (SQLException e) {
      itemDTO.setItemType(null);
    }
    try {
      itemDTO.setMember(createMemberInstance(factory, rs));
    } catch (SQLException e) {
      itemDTO.setMember(null);
    }
    itemDTO.setVersion(rs.getInt("version_item"));
    return itemDTO;
  }


  /**
   * Create an item type instance with the factory and set all its attributes with data selected
   * from the db.
   *
   * @param factory the factory that gives an item type
   * @param rs      the result set that contains item type's data
   * @return a new item type instance with initialized attributes
   */
  public static ItemsTypeDTO createItemsTypeInstance(Factory factory, ResultSet rs)
      throws SQLException {

    //Create ItemType Instance
    ItemsTypeDTO itemsTypeDTO = factory.getItemType();

    //Set Attributes
    itemsTypeDTO.setId(rs.getInt("id_type"));
    itemsTypeDTO.setItemType(StringEscapeUtils.escapeHtml4(rs.getString("item_type")));
    itemsTypeDTO.setVersion(rs.getInt("version_items_type"));
    return itemsTypeDTO;
  }

  /**
   * Create a member instance with the factory and set all its attributes with data selected from
   * the db.
   *
   * @param factory the factory that gives a member
   * @param rs      the result set that contains member's data
   * @return a new member instance with initialized attributes
   */
  public static MemberDTO createMemberInstance(Factory factory, ResultSet rs) throws SQLException {

    //Create Member Instance
    MemberDTO memberDTO = factory.getMember();

    //Set Attributes
    try {
      memberDTO.setId(rs.getInt("id_member"));
    } catch (SQLException e) {
      memberDTO.setId(0);
    }
    try {
      String username = rs.getString("username");
      memberDTO.setUsername(StringEscapeUtils.escapeHtml4(username));
    } catch (SQLException e) {
      memberDTO.setUsername(null);
    }
    try {
      memberDTO.setLastName(StringEscapeUtils.escapeHtml4(rs.getString("last_name")));
    } catch (SQLException e) {
      memberDTO.setLastName(null);
    }
    try {
      memberDTO.setFirstName(StringEscapeUtils.escapeHtml4(rs.getString("first_name")));
    } catch (SQLException e) {
      memberDTO.setFirstName(null);
    }
    try {
      memberDTO.setActualState(StringEscapeUtils.escapeHtml4(rs.getString("state")));
    } catch (SQLException e) {
      memberDTO.setActualState(null);
    }
    try {
      memberDTO.setPhoneNumber(StringEscapeUtils.escapeHtml4(rs.getString("phone")));
    } catch (SQLException e) {
      memberDTO.setPhoneNumber(null);
    }
    try {
      memberDTO.setPassword(StringEscapeUtils.escapeHtml4(rs.getString("password")));
    } catch (SQLException e) {
      memberDTO.setPassword(null);
    }
    try {
      memberDTO.setAdmin(rs.getBoolean("is_admin"));
    } catch (SQLException e) {
      memberDTO.setAdmin(false);
    }
    try {
      memberDTO.setAddress(createAddressInstance(factory, rs));
    } catch (SQLException e) {
      memberDTO.setAddress(null);
    }
    memberDTO.setVersion(rs.getInt("version_member"));
    return memberDTO;
  }

  /**
   * Create an address instance with the factory and set all its attributes with data selected from
   * the db.
   *
   * @param factory the factory that gives an address
   * @param rs      the result set that contains address's data
   * @return a new address instance with initialized attributes
   * @throws SQLException if an attributes is not in the result set
   */
  public static AddressDTO createAddressInstance(Factory factory, ResultSet rs)
      throws SQLException {

    //Create Address Instance
    AddressDTO addressDTO = factory.getAddress();

    //Set Attributes
    addressDTO.setId(rs.getInt("id_address"));
    addressDTO.setStreet(StringEscapeUtils.escapeHtml4(rs.getString("street")));
    addressDTO.setBuildingNumber(
        StringEscapeUtils.escapeHtml4(rs.getString("building_number"))
    );
    addressDTO.setUnitNumber(
        StringEscapeUtils.escapeHtml4(rs.getString("unit_number"))
    );
    addressDTO.setPostcode(StringEscapeUtils.escapeHtml4(rs.getString("postcode")));
    addressDTO.setCommune(StringEscapeUtils.escapeHtml4(rs.getString("commune")));
    addressDTO.setVersion(rs.getInt("version_address"));
    return addressDTO;
  }

  /**
   * Create a refusal instance with the factory and set all its attributes with data selected from
   * the db.
   *
   * @param factory the factory that gives a refusal instance
   * @param rs      the result set that contains refusal's data
   * @return a new refusal instance with initialized attributes
   * @throws SQLException if an attributes is not in the result set
   */
  public static RefusalDTO createRefusalInstance(Factory factory, ResultSet rs)
      throws SQLException {

    //Create Refusal Instance
    RefusalDTO refusalDTO = factory.getRefusal();

    //Set Attributes
    refusalDTO.setIdRefusal(rs.getInt("id_refusal"));
    refusalDTO.setText(rs.getString("text"));
    refusalDTO.setVersion(rs.getInt("version_refusal"));
    return refusalDTO;
  }

  /**
   * Create a rating instance with the factory and set all its attributes with data selected from
   * the db.
   *
   * @param factory the factory that gives a rating instance
   * @param rs      the result set that contains address's data
   * @return a new refusal instance with initialized attributes
   * @throws SQLException if an attributes is not in the result set
   */
  public static RatingDTO createRatingInstance(Factory factory, ResultSet rs) throws SQLException {
    RatingDTO ratingDTO = factory.getRating();
    ratingDTO.setId(rs.getInt("id_rating"));
    ratingDTO.setRating(rs.getInt("rating"));
    ratingDTO.setText(rs.getString("text"));
    ratingDTO.setVersion(rs.getInt("version_rating"));
    try {
      ratingDTO.setMember(createMemberInstance(factory, rs));
    } catch (SQLException e) {
      ratingDTO.setMember(null);
    }
    try {
      ratingDTO.setItem(createItemInstance(factory, rs));
    } catch (SQLException e) {
      ratingDTO.setItem(null);
    }
    return ratingDTO;
  }
}
