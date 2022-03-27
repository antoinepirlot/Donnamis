package be.vinci.pae.ihm.interest;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.ihm.filter.AuthorizeMember;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
   * @param idOffer    the id of the offer
   * @param callWanted true if call wanted false if not
   * @param memberDTO  the member that marks interest
   * @return 1 if interest good added -1 if not
   */
  @POST
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @AuthorizeMember
  public int markInterest(@PathParam("id") int idOffer,
      @QueryParam("call_wanted") boolean callWanted, MemberDTO memberDTO) {

    //Verify if the offer already exist
    if (!interestUCC.offerExist(idOffer)) {
      System.out.println("offer does not exist");
      throw new WebApplicationException("offer not found", Status.NOT_FOUND);
    }

    //Verify the content of the request
    if (memberDTO.getId() < 1) {
      throw new WebApplicationException("idMember required", Status.BAD_REQUEST);
    }

    //Verify if the member already exist
    if (!interestUCC.memberExist(memberDTO)) {
      throw new WebApplicationException("member doesn't exist", Status.NOT_FOUND);
    }

    //Verify if the interest already exist
    if (interestUCC.interestExist(idOffer, memberDTO)) {
      throw new WebApplicationException("interest already exist", Status.CONFLICT);
    }

    //Add the interest
    int res = interestUCC.markInterest(memberDTO, idOffer, callWanted);
    if (res == 0) {
      throw new WebApplicationException(
          "no phone number registered for this member",
          Status.FORBIDDEN);
    } else if (res == -1) {
      throw new WebApplicationException("error ", Status.CONFLICT);
    }
    return res;
  }
}
