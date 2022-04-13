package be.vinci.pae.ihm.interest;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("interests")
public class InterestResource {

  @Inject
  private InterestUCC interestUCC;

  /**
   * Mark the interest in an offer.
   *
   * @param interestDTO the interest to add
   * @return 1 if interest good added -1 if not
   */
  @POST
  @Path("")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public int markInterest(InterestDTO interestDTO) {

    //Verify if the offer already exist
    if (!interestUCC.offerExist(interestDTO.getIdItem())) {
      System.out.println("offer does not exist");
      throw new WebApplicationException("offer not found", Status.NOT_FOUND);
    }

    //Verify the content of the request
    if (interestDTO.getMember().getId() < 1) {
      System.out.println(interestDTO);
      throw new WebApplicationException("idMember required", Status.BAD_REQUEST);
    }

    //Verify if the member already exist
    if (!interestUCC.memberExist(interestDTO.getMember())) {
      throw new WebApplicationException("member doesn't exist", Status.NOT_FOUND);
    }

    //Verify if the interest already exist
    if (interestUCC.interestExist(interestDTO.getIdItem(), interestDTO.getMember())) {
      throw new WebApplicationException("interest already exist", Status.CONFLICT);
    }

    //Add the interest
    int res = interestUCC.markInterest(
        interestDTO.getMember(),
        interestDTO.getIdItem(),
        interestDTO.isCallWanted()
    );
    if (res == 0) {
      throw new WebApplicationException(
          "no phone number registered for this member",
          Status.FORBIDDEN);
    } else if (res == -1) {
      throw new WebApplicationException("error ", Status.INTERNAL_SERVER_ERROR);
    }
    return res;
  }
}
