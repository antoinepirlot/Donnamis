package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.Member;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.member.objects.MemberUCCImpl;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;

  @Inject
  private MemberUCC memberUCC;

  /**
   * Mark the interest in an offer.
   *
   * @param memberDTO  the member
   * @param idOffer    the id of the offer
   * @param callWanted true if member want to be call false if not
   * @return 1 if interest good added -1 if not
   */
  @Override
  public int markInterest(MemberDTO memberDTO, int idOffer, boolean callWanted) {
    LocalDate date = LocalDate.now();
    System.out.println("MARK INTEREST -----------");
    if(callWanted){
      memberDTO = memberUCC.getOneMember(memberDTO.getId());
      System.out.println("PHONE NUMB : " + memberDTO.getPhoneNumber());
      if(memberDTO.getPhoneNumber() == null){
        return 0;
      }
    }
    return interestDAO.markInterest(memberDTO, idOffer, callWanted, date);
  }

  /**
   * Verify if the offer exist in the DB.
   *
   * @param idOffer the if of the offer
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean offerExist(int idOffer) {
    return interestDAO.offerExist(idOffer);
  }

  /**
   * Verify if the member exist in the DB.
   *
   * @param memberDTO the if od the member
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean memberExist(MemberDTO memberDTO) {
    return interestDAO.memberExist(memberDTO);
  }

  /**
   * Verify if the interest exist in the DB.
   *
   * @param idOffer  the id of the offer
   * @param memberDTO the member
   * @return true if exist in the DB false if not
   */
  @Override
  public boolean interestExist(int idOffer, MemberDTO memberDTO) {
    return interestDAO.interestExist(idOffer, memberDTO);
  }

}
