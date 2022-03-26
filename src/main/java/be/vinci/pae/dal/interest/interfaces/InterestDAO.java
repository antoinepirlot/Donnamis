package be.vinci.pae.dal.interest.interfaces;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import java.time.LocalDate;

public interface InterestDAO {

  int markInterest(MemberDTO memberDTO, int idOffer, boolean callWanted, LocalDate date);

  boolean offerExist(int idOffer);

  boolean memberExist(MemberDTO memberDTO);

  boolean interestExist(int idOffer, MemberDTO memberDTO);

}
