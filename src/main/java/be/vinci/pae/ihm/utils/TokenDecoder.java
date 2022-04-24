package be.vinci.pae.ihm.utils;

import be.vinci.pae.exceptions.webapplication.TokenDecodingException;
import be.vinci.pae.exceptions.webapplication.UnauthorizedException;
import be.vinci.pae.ihm.logs.LoggerHandler;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenDecoder {

  private static final Logger logger = LoggerHandler.getLogger();
  private static final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private static final JWTVerifier jwtVerifier = JWT.require(jwtAlgorithm)
      .withIssuer("auth0")
      .build();

  /**
   * Decode the token from the containerRequestContext.
   *
   * @param containerRequestContext contains token
   * @return the decoded token
   */
  public static DecodedJWT decodeToken(ContainerRequestContext containerRequestContext) {
    String token = containerRequestContext.getHeaderString("Authorization");
    DecodedJWT decodedJWT = null;
    if (token == null) {
      String message = "A user try to use a fonction without a valid token";
      logger.log(Level.INFO, message);
      containerRequestContext.abortWith(Response.status(
              Status.UNAUTHORIZED)
          .entity(message)
          .type(MediaType.TEXT_PLAIN)
          .build());
    } else {
      try {
        decodedJWT = jwtVerifier.verify(token);
      } catch (Exception e) {
        String message = "Malformed token : " + e.getMessage() + "\n"
            + Arrays.toString(e.getStackTrace());
        throw new UnauthorizedException(message);
      }
    }
    if (decodedJWT == null) {
      String message = "Null is returned by decode token.";
      throw new TokenDecodingException(message);
    }
    return decodedJWT;
  }
}
