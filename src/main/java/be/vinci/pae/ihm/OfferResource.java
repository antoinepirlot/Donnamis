package be.vinci.pae.ihm;

import be.vinci.pae.biz.OfferUCC;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Path;

@Singleton
@Path("offers")
public class OfferResource {

  private final ObjectMapper jsonMapper = new ObjectMapper();
  @Inject
  private OfferUCC offerUCC;
}
