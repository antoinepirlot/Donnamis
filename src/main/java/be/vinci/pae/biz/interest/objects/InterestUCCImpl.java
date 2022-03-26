package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;

  @Inject
  private MemberUCC memberUCC;

  @Override
  public int markInterest(MemberDTO memberDTO, int idOffer, boolean callWanted) {
    LocalDate date = LocalDate.now();
    System.out.println("MARK INTEREST -----------");
    if (callWanted) {
      memberDTO = memberUCC.getOneMember(memberDTO.getId());
      if (memberDTO.getPhoneNumber() == null) {
        return 0;
      }
    }
    return interestDAO.markInterest(memberDTO, idOffer, callWanted, date);
  }

  @Override
  public boolean offerExist(int idOffer) {
    return interestDAO.offerExist(idOffer);
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO) {
    return interestDAO.memberExist(memberDTO);
  }

  @Override
  public boolean interestExist(int idOffer, MemberDTO memberDTO) {
    return interestDAO.interestExist(idOffer, memberDTO);
  }

}
