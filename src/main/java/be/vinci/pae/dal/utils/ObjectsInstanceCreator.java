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
    System.out.println("Offer instance creation");
    OfferDTO offerDTO = factory.getOffer();
    offerDTO.setId(rs.getInt("id_offer"));
    offerDTO.setDate(rs.getTimestamp("date"));
    offerDTO.setTimeSlot(rs.getString("time_slot"));
    System.out.println("date set");
    offerDTO.setIdItem(rs.getInt("id_item"));
    System.out.println("tile_slot set");
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
    ItemDTO itemDTO = factory.getItem();
    itemDTO.setId(rs.getInt("id_item"));
    try {
      itemDTO.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("title")));
    } catch (SQLException ignored) {
    }
    try {
      itemDTO.setPhoto(StringEscapeUtils.escapeHtml4(rs.getString("photo")));
    } catch (SQLException ignored) {
    }
    try {
      itemDTO.setLastOfferDate(rs.getTimestamp("last_offer_date"));
    } catch (SQLException ignored) {
    }
    try {
      itemDTO.setItemDescription(
          StringEscapeUtils.escapeHtml4(rs.getString("item_description"))
      );
    } catch (SQLException ignored) {
    }
    try {
      itemDTO.setOfferStatus(
          StringEscapeUtils.escapeHtml4(rs.getString("offer_status"))
      );
    } catch (SQLException ignored) {
    }
    try {
      itemDTO.setItemType(createItemsTypeInstance(factory, rs));
    } catch (SQLException e) {
      itemDTO.setItemType(null);
    }
    try {
      itemDTO.setMember(createMemberInstance(factory, rs));
    } catch (SQLException e) {
      System.out.println("No member for the item.");
    }
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
    System.out.println("Setting all item type attributes");
    ItemsTypeDTO itemsTypeDTO = factory.getItemType();
    itemsTypeDTO.setId(rs.getInt("id_type"));
    itemsTypeDTO.setItemType(StringEscapeUtils.escapeHtml4(rs.getString("item_type")));
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
    System.out.println("Setting all member attributes");
    MemberDTO memberDTO = factory.getMember();
    memberDTO.setId(rs.getInt("id_member"));
    memberDTO.setUsername(StringEscapeUtils.escapeHtml4(rs.getString("username")));
    memberDTO.setLastName(StringEscapeUtils.escapeHtml4(rs.getString("last_name")));
    memberDTO.setFirstName(StringEscapeUtils.escapeHtml4(rs.getString("first_name")));
    try {
      memberDTO.setActualState(StringEscapeUtils.escapeHtml4(rs.getString("state")));
    } catch (SQLException e) {
      System.out.println("No actual state selected for this member");
    }
    try {
      memberDTO.setPhoneNumber(StringEscapeUtils.escapeHtml4(rs.getString("phone")));
    } catch (SQLException e) {
      System.out.println("No phone selected for this member");
    }
    try {
      memberDTO.setPassword(StringEscapeUtils.escapeHtml4(rs.getString("password")));
    } catch (SQLException e) {
      System.out.println("No password selected for this member");
    }
    try {
      memberDTO.setAdmin(rs.getBoolean("is_admin"));
    } catch (SQLException e) {
      System.out.println("No is_admin selected for this member");
    }
    try {
      memberDTO.setAddress(createAddressInstance(factory, rs));
    } catch (SQLException e) {
      System.out.println("No address selected for this member");
    }
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
    System.out.println("setting all address attributes");
    AddressDTO addressDTO = factory.getAddress();
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
    RefusalDTO refusalDTO = factory.getRefusal();
    refusalDTO.setIdRefusal(rs.getInt("id_refusal"));
    refusalDTO.setText(rs.getString("text"));
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
    try {
      ratingDTO.setMember(createMemberInstance(factory, rs));
    } catch (SQLException e) {
      ratingDTO.setMember(null);
    }
    ratingDTO.setItem(createItemInstance(factory, rs));
    return ratingDTO;
  }
}
