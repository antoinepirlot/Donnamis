package be.vinci.pae.biz.address.objects;

import be.vinci.pae.biz.address.interfaces.Address;

public class AddressImpl implements Address {

  private int id;
  private String street;
  private String buildingNumber;
  private String unitNumber;
  private String postcode;
  private String commune;

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String getStreet() {
    return street;
  }

  @Override
  public void setStreet(String street) {
    this.street = street;
  }

  @Override
  public String getBuildingNumber() {
    return buildingNumber;
  }

  @Override
  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  @Override
  public String getUnitNumber() {
    return unitNumber;
  }

  @Override
  public void setUnitNumber(String unitNumber) {
    this.unitNumber = unitNumber;
  }

  @Override
  public String getPostcode() {
    return postcode;
  }

  @Override
  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  @Override
  public String getCommune() {
    return commune;
  }

  @Override
  public void setCommune(String commune) {
    this.commune = commune;
  }

  @Override
  public String toString() {
    return "Member{"
        + "id=" + id
        + ", street='" + street + '\''
        + ", buildingNumber='" + buildingNumber + '\''
        + ", unitNumber='" + unitNumber + '\''
        + ", postcode='" + postcode + '\''
        + ", commune=" + commune
        + '}';
  }
}
