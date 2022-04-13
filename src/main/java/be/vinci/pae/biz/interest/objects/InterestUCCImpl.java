package be.vinci.pae.biz.interest.objects;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import jakarta.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class InterestUCCImpl implements InterestUCC {

  @Inject
  private InterestDAO interestDAO;
  @Inject
  private DALServices dalServices;

  @Override
  public int markInterest(MemberDTO memberDTO, int idItem, boolean callWanted) {
    dalServices.start();
    Timestamp date = Timestamp.valueOf(LocalDateTime.now());
    int res = interestDAO.markInterest(memberDTO, idItem, callWanted, date);
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
  public boolean interestExist(int idItem, MemberDTO memberDTO) {
    dalServices.start();
    if (!interestDAO.interestExist(idItem, memberDTO)) {
      dalServices.rollback();
      return false;
    }
    dalServices.commit();
    return true;
  }

}
