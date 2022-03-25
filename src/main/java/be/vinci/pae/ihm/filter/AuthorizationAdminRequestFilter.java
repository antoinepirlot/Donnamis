package be.vinci.pae.ihm.filter;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.ihm.filter.utils.TokenDecoder;
import be.vinci.pae.ihm.logs.LoggerHandler;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Provider
@AuthorizeAdmin
public class AuthorizationAdminRequestFilter implements ContainerRequestFilter {

  private final Logger logger;
  @Inject
  private MemberUCC memberUCC;

  public AuthorizationAdminRequestFilter() {

    logger = LoggerHandler.getLogger();
  }


  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    DecodedJWT decodedJWT = TokenDecoder.decodeToken(containerRequestContext);
    int id = decodedJWT.getClaim("id").asInt();
    MemberDTO authenticatedMember = memberUCC.getOneMember(id);
    if (!authenticatedMember.isAdmin()) {
      String message = "The member with id: " + id + " can't access a admin method";
      this.logger.log(Level.INFO, message);
      containerRequestContext.abortWith(Response.status(Status.FORBIDDEN)
          .entity(message)
          .type(MediaType.TEXT_PLAIN)
          .build());
    }
    containerRequestContext.setProperty("authorizedMember", authenticatedMember);
  }
}
