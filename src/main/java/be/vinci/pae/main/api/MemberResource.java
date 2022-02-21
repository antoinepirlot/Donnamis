package be.vinci.pae.main.api;

import be.vinci.pae.main.domain.Member;
import be.vinci.pae.main.services.MemberDAO;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Root resource (exposed at "myresource" path)
 */
@Singleton
@Path("members")
public class MemberResource {

  private MemberDAO myMemberDAO = new MemberDAO();


  private Member[] defaultTexts = {
      new Member(1, "Navial", "pswd", "Denis", "Victor", true, "confirmed", "0479 48 91 37"),
      new Member(2, "Swapiz", "pswd2Ouf", "Hernaut", "Loic", false, "confirmed", "0478 24 95 44"),
  };
  private List<Member> members = new ArrayList<>(
      Arrays.asList(defaultTexts)); // to get a changeable list, asList is fixed size

  /**
   * Method handling HTTP GET requests. The returned object will be sent to the client as
   * "text/plain" media type.
   *
   * @return String that will be returned as a text/plain response.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<Member> getAll() {
    return myMemberDAO.getAll();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Member createOne(Member member) {
    return myMemberDAO.createOne(member);
  }

}