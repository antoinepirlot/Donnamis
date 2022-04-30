package be.vinci.pae.biz.address.interfaces;

import be.vinci.pae.biz.address.objects.AddressImpl;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = AddressImpl.class)
public interface AddressDTO {

  int getId();

  void setId(int id);

  String getStreet();

  void setStreet(String street);

  String getBuildingNumber();

  void setBuildingNumber(String buildingNumber);

  String getUnitNumber();

  void setUnitNumber(String unitNumber);

  String getPostcode();

  void setPostcode(String postcode);

  String getCommune();

  void setCommune(String commune);

  int getVersion();

  void setVersion(int version);
}
