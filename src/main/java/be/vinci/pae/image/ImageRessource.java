package be.vinci.pae.image;

import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.maven.surefire.shared.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

public class ImageRessource {

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @AuthorizeMember
  public Response uploadFile(@FormDataParam("file") InputStream file,
      @FormDataParam("file") FormDataContentDisposition fileDisposition) throws IOException {
    UUID uuid = UUID.randomUUID();
    String fileName = String.valueOf(uuid);
    String extension = FilenameUtils.getExtension(fileDisposition.getName());
    fileName += extension;
    Files.copy(file, Paths.get(fileName));

    return Response.ok().build();
  }


}
