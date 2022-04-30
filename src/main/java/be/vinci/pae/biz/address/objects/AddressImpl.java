package be.vinci.pae.biz.address.objects;

import be.vinci.pae.biz.address.interfaces.Address;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(Include.NON_NULL)
public class AddressImpl implements Address {

  @JsonView(Views.Public.class)
  private int id;
  @JsonView(Views.Public.class)
  private String street;
  @JsonView(Views.Public.class)
  private String buildingNumber;
  @JsonView(Views.Public.class)
  private String unitNumber;
  @JsonView(Views.Public.class)
  private String postcode;
  @JsonView(Views.Public.class)
  private String commune;
  @JsonView(Views.Public.class)
  private int version;

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
  public int getVersion() {
    return version;
  }

  @Override
  public void setVersion(int version) {
    this.version = version;
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
