package be.vinci.pae.utils;

import be.vinci.pae.biz.factory.Factory;
import be.vinci.pae.biz.factory.FactoryImpl;
import be.vinci.pae.biz.interfaces.item.ItemUCC;
import be.vinci.pae.biz.objects.item.ItemUCCImpl;
import be.vinci.pae.biz.interfaces.member.MemberUCC;
import be.vinci.pae.biz.objects.member.MemberUCCImpl;
import be.vinci.pae.biz.interfaces.offer.OfferUCC;
import be.vinci.pae.biz.objects.offer.OfferUCCImpl;
import be.vinci.pae.biz.interfaces.interest.InterestUCC;
import be.vinci.pae.biz.objects.interest.InterestUCCImpl;
import be.vinci.pae.dal.services.DALServices;
import be.vinci.pae.dal.services.DALServicesImpl;
import be.vinci.pae.dal.interfaces.item.ItemDAO;
import be.vinci.pae.dal.objects.item.ItemDAOImpl;
import be.vinci.pae.dal.interfaces.member.MemberDAO;
import be.vinci.pae.dal.objects.member.MemberDAOImpl;
import be.vinci.pae.dal.interfaces.offer.OfferDAO;
import be.vinci.pae.dal.objects.offer.OfferDAOImpl;
import be.vinci.pae.dal.interfaces.interest.InterestDAO;
import be.vinci.pae.dal.objects.interest.InterestDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(DALServicesImpl.class).to(DALServices.class).in(Singleton.class);
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);

    //Member
    bind(MemberDAOImpl.class).to(MemberDAO.class).in(Singleton.class);
    bind(MemberUCCImpl.class).to(MemberUCC.class).in(Singleton.class);
    bind(ItemUCCImpl.class).to(ItemUCC.class).in(Singleton.class);
    bind(ItemDAOImpl.class).to(ItemDAO.class).in(Singleton.class);
    bind(OfferUCCImpl.class).to(OfferUCC.class).in(Singleton.class);
    bind(OfferDAOImpl.class).to(OfferDAO.class).in(Singleton.class);

    //Interest
    bind(InterestDAOImpl.class).to(InterestDAO.class).in(Singleton.class);
    bind(InterestUCCImpl.class).to(InterestUCC.class).in(Singleton.class);
  }
}
