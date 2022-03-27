package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;
  @Inject
  private DALServices dalServices;

  @Inject
  private MemberUCC memberUCC;

  @Override
  public int markInterest(MemberDTO memberDTO, int idOffer, boolean callWanted) {
    //TODO
    dalServices.start();
    LocalDate date = LocalDate.now();
    int res = interestDAO.markInterest(idMember, idOffer, callWanted, date);
    if (res == -1) {
      dalServices.rollback();
    } else {
      dalServices.commit();
    }
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
    dalServices.start();
    if (!interestDAO.offerExist(idOffer)) {
      dalServices.rollback();
      return false;
    }
    dalServices.commit();
    return true;
  }

  @Override
  public boolean memberExist(MemberDTO memberDTO) {
    dalServices.start();
    if (!interestDAO.memberExist(memberDTO)) {
      dalServices.rollback();
    }
    dalServices.commit();
    return true;
  }

  @Override
  public boolean interestExist(int idOffer, MemberDTO memberDTO) {
    dalServices.start();
    if (!interestDAO.interestExist(idOffer, memberDTO)) {
      dalServices.rollback();
    }
    dalServices.commit();
    return true;
  }

}
