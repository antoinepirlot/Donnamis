package be.vinci.pae.ihm.filter.utils;

import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.views.Views;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class Json<T> {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  private final Class<T> type;

  public Json(Class<T> type) {
    this.type = type;
  }

  /**
   * Filter all attributes from the T class from the list.
   * @param list the list where objects' attributes must be filtered
   * @return the filtered object's attributes list
   */
  public List<T> filterPublicJsonViewAsList(List<T> list) {
    if (list == null) {
      return null;
    }
    try {
      JavaType type = jsonMapper.getTypeFactory().constructCollectionType(List.class, this.type);
      // serialize using JSON Views : public view (all fields not required in the
      // views are not serialized)
      String publicItemListAsString = this.jsonMapper.writerWithView(Views.Public.class)
          .writeValueAsString(list);
      // deserialize using JSON Views : Public View (all fields that are not serialized
      // are set to their default values in the POJOs)
      return this.jsonMapper.readerWithView(Views.Public.class).forType(type)
          .readValue(publicItemListAsString);
    } catch (JsonProcessingException e) {
      throw new FatalException(e);
    }
  }

  /**
   * Filter the object's attributes.
   * @param object the object to filter
   * @return filtered object
   */
  public T filterPublicJsonView(T object) {
    try {
      // serialize using JSON Views : public view (all fields not required in the
      // views are not serialized)
      String publicItemAsString = this.jsonMapper.writerWithView(Views.Public.class)
          .writeValueAsString(object);
      // deserialize using JSON Views : Public View (all fields that are not serialized
      // are set to their default values in the POJO)
      return this.jsonMapper.readerWithView(Views.Public.class).forType(type)
          .readValue(publicItemAsString);
    } catch (JsonProcessingException e) {
      throw new FatalException(e);
    }

  }
}
