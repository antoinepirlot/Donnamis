package be.vinci.pae.ihm.image;

import be.vinci.pae.ihm.filter.AuthorizeMember;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.maven.surefire.shared.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Singleton
@Path("upload")
public class ImageRessource {

  @POST
  @Path("image")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) throws IOException {
    System.out.println("*********************************");

    String path = Config.getProperty("photoPath");
    System.out.println("File : " + file);
    System.out.println("File dispo : " + fileDisposition);
    UUID uuid = UUID.randomUUID();
    String fileName = String.valueOf(uuid);
    String extension = FilenameUtils.getExtension(fileDisposition.getFileName());

    fileName += "." + extension;
    path += fileName;
    //System.out.println("Path : " + path);
    Files.copy(file, Paths.get(fileName));

    Files.copy(file, java.nio.file.Path.of(path));
    return Response.ok().build();
  }
}
