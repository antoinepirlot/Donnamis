package be.vinci.pae.utils;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.interest.objects.InterestUCCImpl;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.item.objects.ItemUCCImpl;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.member.objects.MemberUCCImpl;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.biz.offer.objects.OfferUCCImpl;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.interest.objects.InterestDAOImpl;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.item.objects.ItemDAOImpl;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.member.objects.MemberDAOImpl;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.offer.objects.OfferDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    //MemberUCC tests
    bind(MemberUCCImpl.class).to(MemberUCC.class).in(Singleton.class);
    bind(Mockito.mock(MemberDAOImpl.class)).to(MemberDAO.class);

    //InterestUCC tests
    bind(InterestUCCImpl.class).to(InterestUCC.class).in(Singleton.class);
    bind(Mockito.mock(InterestDAOImpl.class)).to(InterestDAO.class);
    bind(Mockito.mock(MemberUCCImpl.class)).to(MemberUCC.class);

    //ItemUCC tests
    bind(ItemUCCImpl.class).to(ItemUCC.class).in(Singleton.class);
    bind(Mockito.mock(ItemDAOImpl.class)).to(ItemDAO.class);

    //OfferUCC tests
    bind(OfferUCCImpl.class).to(OfferUCC.class).in(Singleton.class);
    bind(Mockito.mock(OfferDAOImpl.class)).to(OfferDAO.class);
  }
}
