package be.vinci.pae.main;

import be.vinci.pae.exceptions.mapper.ExceptionMapper;
import be.vinci.pae.ihm.logs.LoggerHandler;
import be.vinci.pae.utils.ApplicationBinder;
import be.vinci.pae.utils.Config;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 */
public class Main {

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   *
   * @return Grizzly HTTP server.
   */
  public static HttpServer startServer() {
    // create a resource config that scans for JAX-RS resources and providers
    // in be.vinci package
    final ResourceConfig rc = new ResourceConfig().packages("be.vinci.pae.ihm")
        .register(JacksonFeature.class)
        .register(ApplicationBinder.class)
        .register(ExceptionMapper.class)
        .register(MultiPartFeature.class);

    // create and start a new instance of grizzly http server
    // exposing the Jersey application at BASE_URI
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(Config.getProperty("BaseUri")), rc);


  }

  /**
   * Main method.
   *
   * @param args list of arguments
   * @throws IOException problem execution
   */
  public static void main(String[] args) throws IOException {
    try {
      Config.load("dev.properties");
      LoggerHandler.init();
      LoggerHandler.getLogger().log(Level.INFO, "Server is starting.");
      final HttpServer server = startServer();
      System.out.println(String.format("Jersey app started with endpoints available at "
          + "%s%nHit Ctrl-C to stop it...", Config.getProperty("BaseUri")));
      System.in.read();
      server.stop();
      LoggerHandler.getLogger().log(Level.INFO, "Server is stopped.");
    } catch (IOException e) {
      LoggerHandler.getLogger().log(Level.SEVERE, "IOException");
    } finally {
      LoggerHandler.close();
    }
  }
}

