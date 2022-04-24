package be.vinci.pae.ihm.image;

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
public class ImageRessource {

  private Logger logger = LoggerHandler.getLogger();

  @POST
  @Path("upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) throws IOException {
    String path = Config.getProperty("photoPath");
    UUID uuid = UUID.randomUUID();
    String extension = FilenameUtils.getExtension(fileDisposition.getFileName());
    String fileName = path + "\\" + String.valueOf(uuid) + "." + extension;
    try (FileOutputStream fos = new FileOutputStream(fileName)) {
      int c;
      while ((c = file.read()) != -1) {
        fos.write(c);
      }
      this.logger.log(Level.INFO, "An image has been copied.");
    }
  }
}
