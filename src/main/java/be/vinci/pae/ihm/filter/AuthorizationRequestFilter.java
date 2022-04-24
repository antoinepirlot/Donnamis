package be.vinci.pae.ihm.filter;

import be.vinci.pae.ihm.utils.TokenDecoder;
import jakarta.inject.Singleton;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Singleton
@Provider
@AuthorizeMember
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  public AuthorizationRequestFilter() {
  }

  /**
   * Decode token.
   * @param containerRequestContext the container request
   */
  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    TokenDecoder.decodeToken(containerRequestContext);
  }
}
