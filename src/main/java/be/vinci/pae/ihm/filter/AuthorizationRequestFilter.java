package be.vinci.pae.ihm.filter;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.exceptions.webapplication.ForbiddenException;
import be.vinci.pae.exceptions.webapplication.UnauthorizedException;
import be.vinci.pae.ihm.logs.LoggerHandler;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;

@Singleton
@Provider
@Authorize
public class AuthorizationRequestFilter implements ContainerRequestFilter {

  private final Algorithm jwtAlgorithm;
  private final JWTVerifier jwtVerifier;
  private Logger logger;

  public AuthorizationRequestFilter() {
    jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
    jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
    logger = LoggerHandler.getLogger();
  }

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {
    String token = containerRequestContext.getHeaderString("Authorization");
    if (token == null) {
      containerRequestContext.abortWith(Response.status(
              Status.UNAUTHORIZED)
          .entity("A token is needed.")
          .build());
      String message = "A user try to use a fonction without a valid token";
      this.logger.log(Level.INFO, message);
    } else {
      DecodedJWT decodedJWT = null;
      try {
        decodedJWT = this.jwtVerifier.verify(token);
      } catch (Exception e) {
        String message = "Malformed token : " + e.getMessage() + "\n"
            + Arrays.toString(e.getStackTrace());
        this.logger.log(Level.INFO, message);
        throw new UnauthorizedException(message);
      }
    }
  }
}
