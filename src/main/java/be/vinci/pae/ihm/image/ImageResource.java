package be.vinci.pae.ihm.image;

import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.ihm.logs.LoggerHandler;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.surefire.shared.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Singleton
@Path("images")
@AuthorizeMember
public class ImageResource {

  private final Logger logger = LoggerHandler.getLogger();
  private static final String[] ALLOWED_EXTENSIONS = {"jpg", "png"};

  @Inject
  private ItemUCC itemUCC;

  @POST
  @Path("upload/{idItem}")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @AuthorizeMember
  public void uploadFile(@PathParam("idItem") int idItem, @FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) {
    String extension = FilenameUtils.getExtension(fileDisposition.getFileName());
    if (!checkExtension(extension)) {
      throw new WrongBodyDataException("The file extension is not correct.");
    }
    String path = Config.getPhotoPath();
    UUID uuid = UUID.randomUUID();
    String photoPath = path + "\\" + uuid + "." + extension;
    try {
      Files.copy(file, Paths.get(photoPath));
      if (!this.itemUCC.addPhoto(idItem, uuid + "." + extension)) {
        throw new FatalException("The image hasn't been added into the database");
      }
    }catch (IOException e){
      throw new FatalException(e);
    }
    this.logger.log(Level.INFO, "An image has been copied.");
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
