package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.exceptions.webapplication.ConflictException;
import be.vinci.pae.exceptions.webapplication.ObjectNotFoundException;
import jakarta.inject.Inject;
import java.sql.SQLException;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;
  @Inject
  private OfferUCC offerUCC;
  @Inject
  private MemberUCC memberUCC;
  @Inject
  private DALServices dalServices;

  @Override
  public void markInterest(InterestDTO interestDTO) {
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
    if (this.interestExist(interestDTO)) {
      throw new ConflictException("interest already exist");
    }
    try {
      dalServices.start();
      boolean isDone = interestDAO.markInterest(interestDTO);
      dalServices.commit();
      if (!isDone) {
        throw new FatalException("The interest hasn't been added");
      }
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }

  @Override
  public boolean interestExist(InterestDTO interestDTO) {
    try {
      dalServices.start();
      boolean interestExist = this.interestDAO.interestExist(interestDTO);
      dalServices.commit();
      return interestExist;
    } catch (SQLException e) {
      dalServices.rollback();
      throw new FatalException(e);
    }
  }
}
