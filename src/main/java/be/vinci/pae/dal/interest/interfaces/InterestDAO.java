package be.vinci.pae.dal.interest.interfaces;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import java.sql.Timestamp;

public interface InterestDAO {

  int markInterest(MemberDTO memberDTO, int idItem, boolean callWanted, Timestamp date);

  boolean offerExist(int idOffer);

  boolean memberExist(MemberDTO memberDTO);

  boolean interestExist(int idItem, MemberDTO memberDTO);

}
