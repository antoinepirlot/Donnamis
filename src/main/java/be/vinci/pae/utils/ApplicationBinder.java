package be.vinci.pae.utils;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.FactoryImpl;
import be.vinci.pae.biz.ItemUCC;
import be.vinci.pae.biz.ItemUCCImpl;
import be.vinci.pae.biz.MemberUCC;
import be.vinci.pae.biz.MemberUCCImpl;
import be.vinci.pae.biz.OfferUCC;
import be.vinci.pae.biz.OfferUCCImpl;
import be.vinci.pae.biz.interest.InterestUCC;
import be.vinci.pae.biz.interest.InterestUCCImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.DALServicesImpl;
import be.vinci.pae.dal.ItemDAO;
import be.vinci.pae.dal.ItemDAOImpl;
import be.vinci.pae.dal.MemberDAO;
import be.vinci.pae.dal.MemberDAOImpl;
import be.vinci.pae.dal.OfferDAO;
import be.vinci.pae.dal.OfferDAOImpl;
import be.vinci.pae.dal.interest.InterestDAO;
import be.vinci.pae.dal.interest.InterestDAOImpl;
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
