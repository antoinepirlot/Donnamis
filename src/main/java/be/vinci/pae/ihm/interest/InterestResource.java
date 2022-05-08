package be.vinci.pae.ihm.interest;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Singleton
@Path("interests")
@AuthorizeMember
public class InterestResource {

  @Inject
  private InterestUCC interestUCC;

  /////////////////////////////////////////////////////////
  ///////////////////////POST//////////////////////////////
  /////////////////////////////////////////////////////////

  /**
   * Mark the interest in an offer.
   *
   * @param interestDTO the interest to add
   */
  @POST
  @Path("")
  @Consumes(MediaType.APPLICATION_JSON)
  public void markInterest(InterestDTO interestDTO) {

    //Verify the content of the request
    if (interestDTO == null
        || interestDTO.getOffer() == null || interestDTO.getOffer().getId() < 1
        || interestDTO.getMember() == null || interestDTO.getMember().getId() < 1
    ) {
      throw new WrongBodyDataException("Wrong body");
    }
    interestDTO.setDate(Timestamp.valueOf(LocalDateTime.now()));
    //Add the interest
    interestUCC.markInterest(interestDTO);
  }
}
