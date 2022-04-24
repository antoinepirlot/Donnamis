package be.vinci.pae.utils;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

  private static Properties props;
  private static Properties oneDriveProps;

  /**
   * Load the properties file from the specified file path.
   * @param file the specified properties file path
   */
  public static void load(String file) {
    props = new Properties();
    oneDriveProps = new Properties();
    try (
        InputStream input = new FileInputStream(file);
        InputStream oneDriveInput = new FileInputStream("onedrive.properties")
    ) {
      props.load(input);
      oneDriveProps.load(oneDriveInput);
    } catch (IOException e) {
      throw new WebApplicationException(
          Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type("text/plain")
              .build());
    }
  }

  public static String getProperty(String key) {
    return props.getProperty(key);
  }

  public static String getPhotoPath() {
    return oneDriveProps.getProperty("photoPath");
  }

  public static Integer getIntProperty(String key) {
    return Integer.parseInt(props.getProperty(key));
  }

  public static boolean getBoolProperty(String key) {
    return Boolean.parseBoolean(props.getProperty(key));
  }
}
