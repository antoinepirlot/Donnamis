package be.vinci.pae.ihm.image;

import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.logs.LoggerHandler;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.surefire.shared.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Singleton
@Path("image")
@AuthorizeMember
public class ImageResource {

  private final Logger logger = LoggerHandler.getLogger();
  private static final String[] ALLOWED_EXTENSIONS = {"jpg", "png"};

  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) throws IOException {
    String extension = FilenameUtils.getExtension(fileDisposition.getFileName());
    if (!checkExtension(extension)) {
      throw new WrongBodyDataException("The file extension is not correct.");
    }
    String path = Config.getProperty("photoPath");
    UUID uuid = UUID.randomUUID();
    String fileName = path + "\\" + uuid + "." + extension;
    try (FileOutputStream fos = new FileOutputStream(fileName)) {
      int c;
      while ((c = file.read()) != -1) {
        fos.write(c);
      }
      this.logger.log(Level.INFO, "An image has been copied.");
    }
  }

  private boolean checkExtension(String fileExtension) {
    for (String ext : ALLOWED_EXTENSIONS) {
      if (ext.equals(fileExtension)) {
        return true;
      }
    }
    return false;
  }
}
