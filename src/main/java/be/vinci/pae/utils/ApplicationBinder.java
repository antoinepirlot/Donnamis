package be.vinci.pae.utils;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.factory.objects.FactoryImpl;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.item.objects.ItemUCCImpl;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.member.objects.MemberUCCImpl;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.biz.offer.objects.OfferUCCImpl;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.interest.objects.InterestUCCImpl;
import dal.services.interfaces.DALServices;
import dal.services.objects.DALServicesImpl;
import dal.item.interfaces.ItemDAO;
import dal.item.objects.ItemDAOImpl;
import dal.member.interfaces.MemberDAO;
import dal.member.objects.MemberDAOImpl;
import dal.offer.interfaces.OfferDAO;
import dal.offer.objects.OfferDAOImpl;
import dal.interest.interfaces.InterestDAO;
import dal.interest.objects.InterestDAOImpl;
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

    //Item
    bind(ItemUCCImpl.class).to(ItemUCC.class).in(Singleton.class);
    bind(ItemDAOImpl.class).to(ItemDAO.class).in(Singleton.class);

    //Interest
    bind(InterestDAOImpl.class).to(InterestDAO.class).in(Singleton.class);
    bind(InterestUCCImpl.class).to(InterestUCC.class).in(Singleton.class);

    //Offer
    bind(OfferUCCImpl.class).to(OfferUCC.class).in(Singleton.class);
    bind(OfferDAOImpl.class).to(OfferDAO.class).in(Singleton.class);
  }
}
