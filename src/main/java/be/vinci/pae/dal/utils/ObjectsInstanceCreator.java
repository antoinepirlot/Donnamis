package be.vinci.pae.dal.utils;

import be.vinci.pae.biz.address.interfaces.AddressDTO;
import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.item.interfaces.ItemDTO;
import be.vinci.pae.biz.itemstype.interfaces.ItemTypeDTO;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
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
    offerDTO.setIdOffer(rs.getInt("id_offer"));
    offerDTO.setDate(rs.getDate("date"));
    offerDTO.setTimeSlot(rs.getString("time_slot"));
    offerDTO.setItem(createItemInstance(factory, rs));
    System.out.println("date set");
    offerDTO.setTimeSlot(rs.getString("time_slot"));
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
  public static ItemDTO createItemInstance(Factory factory, ResultSet rs) {
    System.out.println("Setting all item attributes");
    ItemDTO itemDTO = factory.getItem();
    try {
      itemDTO.setId(rs.getInt("id_item"));
      itemDTO.setTitle(StringEscapeUtils.escapeHtml4(rs.getString("title")));
      itemDTO.setPhoto(StringEscapeUtils.escapeHtml4(rs.getString("photo")));
      itemDTO.setItemDescription(
          StringEscapeUtils.escapeHtml4(rs.getString("item_description"))
      );
      itemDTO.setOfferStatus(
          StringEscapeUtils.escapeHtml4(rs.getString("offer_status"))
      );
      itemDTO.setItemType(createItemTypeInstance(factory, rs));
      itemDTO.setMember(createMemberInstance(factory, rs));
    } catch (SQLException e) {
      e.printStackTrace();
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
  private static ItemTypeDTO createItemTypeInstance(Factory factory, ResultSet rs) {
    System.out.println("Setting all item type attributes");
    ItemTypeDTO itemTypeDTO = factory.getItemType();
    try {
      itemTypeDTO.setId(rs.getInt("id_type"));
      itemTypeDTO.setItemType(StringEscapeUtils.escapeHtml4(rs.getString("item_type")));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return itemTypeDTO;
  }

  /**
   * Create a member instance with the factory and set all its attributes with data selected from
   * the db.
   *
   * @param factory the factory that gives a member
   * @param rs      the result set that contains member's data
   * @return a new member instance with initialized attributes
   */
  public static MemberDTO createMemberInstance(Factory factory, ResultSet rs) {
    System.out.println("Setting all member attributes");
    MemberDTO memberDTO = factory.getMember();
    try {
      memberDTO.setUsername(StringEscapeUtils.escapeHtml4(rs.getString("username")));
      memberDTO.setLastName(StringEscapeUtils.escapeHtml4(rs.getString("last_name")));
      memberDTO.setFirstName(StringEscapeUtils.escapeHtml4(rs.getString("first_name")));
      try {
        memberDTO.setId(rs.getInt("id_member"));
      } catch (SQLException e) {
        System.out.println("No id selected for this member");
      }
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
    } catch (SQLException e) {
      e.printStackTrace();
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
  private static AddressDTO createAddressInstance(Factory factory, ResultSet rs)
      throws SQLException {
    System.out.println("setting all address attributes");
    AddressDTO addressDTO = factory.getAddress();
    addressDTO.setId(rs.getInt("id_address"));
    addressDTO.setStreet(StringEscapeUtils.escapeHtml4("street"));
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

}
