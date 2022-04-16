package be.vinci.pae.ihm.interest;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import be.vinci.pae.exceptions.webapplication.WrongBodyDataException;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.rmi.UnexpectedException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Singleton
@Path("interests")
public class InterestResource {

  @Inject
  private InterestUCC interestUCC;
  @Inject
  private OfferUCC offerUCC;
  @Inject
  private MemberUCC memberUCC;

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
  @AuthorizeMember
  public void markInterest(InterestDTO interestDTO) throws SQLException, UnexpectedException {
    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
    //Verify the content of the request
    if (interestDTO == null
        || interestDTO.getOffer() == null || interestDTO.getOffer().getId() < 1
        || interestDTO.getMember() == null || interestDTO.getMember().getId() < 1) {
      throw new WrongBodyDataException("idMember required");
    }
    interestDTO.setDate(now);
    //Verify if the offer already exist
    if (!this.offerUCC.offerExist(interestDTO.getOffer())) {
      System.out.println("offer does not exist");
      throw new ObjectNotFoundException("offer not found");
    }

    //Verify if the member already exist
    if (!this.memberUCC.memberExist(interestDTO.getMember(), -1)) {
      throw new ObjectNotFoundException("member doesn't exist");
    }

    //Verify if the interest already exist
    if (this.interestUCC.interestExist(interestDTO)) {
      throw new ConflictException("interest already exist");
    }
    //Add the interest
    if (!interestUCC.markInterest(interestDTO)) {
      throw new UnexpectedException("The interest hasn't been added");
    }
  }
}
