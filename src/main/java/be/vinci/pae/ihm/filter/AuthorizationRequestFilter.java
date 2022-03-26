package be.vinci.pae.ihm.filter;

import be.vinci.pae.ihm.filter.utils.TokenDecoder;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  public AuthorizationRequestFilter() {
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    TokenDecoder.decodeToken(containerRequestContext);
  }
}
