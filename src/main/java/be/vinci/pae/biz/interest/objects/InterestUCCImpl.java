package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.time.LocalDate;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;
  @Inject
  private DALServices dalServices;

  @Inject
  private MemberDAO memberDAO;

  @Override
  public int markInterest(MemberDTO memberDTO, int idOffer, boolean callWanted) {
    dalServices.start();
    LocalDate date = LocalDate.now();
    int res = interestDAO.markInterest(memberDTO, idOffer, callWanted, date);
    if (res == -1) {
      dalServices.rollback();
      return -1;
    }
    dalServices.commit();
    return 1;
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
      return false;
    }
    dalServices.commit();
    return true;
  }

  @Override
  public boolean interestExist(int idOffer, MemberDTO memberDTO) {
    dalServices.start();
    if (!interestDAO.interestExist(idOffer, memberDTO)) {
      dalServices.rollback();
      return false;
    }
    dalServices.commit();
    return true;
  }

}
