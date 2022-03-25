package be.vinci.pae.ihm.filter;

import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.ihm.filter.utils.TokenDecoder;
import be.vinci.pae.ihm.logs.LoggerHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
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
    TokenDecoder.decodeToken(containerRequestContext);
  }
}
