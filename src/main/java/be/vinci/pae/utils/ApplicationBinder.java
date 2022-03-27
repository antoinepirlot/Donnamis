package be.vinci.pae.utils;

import be.vinci.pae.biz.factory.interfaces.Factory;
import be.vinci.pae.biz.factory.objects.FactoryImpl;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.interest.objects.InterestUCCImpl;
import be.vinci.pae.biz.item.interfaces.ItemUCC;
import be.vinci.pae.biz.item.objects.ItemUCCImpl;
import be.vinci.pae.biz.itemstype.interfaces.ItemsTypeUCC;
import be.vinci.pae.biz.itemstype.objects.ItemsTypeUCCImpl;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.member.objects.MemberUCCImpl;
import be.vinci.pae.biz.offer.interfaces.OfferUCC;
import be.vinci.pae.biz.offer.objects.OfferUCCImpl;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.interest.objects.InterestDAOImpl;
import be.vinci.pae.dal.item.interfaces.ItemDAO;
import be.vinci.pae.dal.item.objects.ItemDAOImpl;
import be.vinci.pae.dal.itemstype.interfaces.ItemsTypeDAO;
import be.vinci.pae.dal.itemstype.objects.ItemsTypeDAOImpl;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.member.objects.MemberDAOImpl;
import be.vinci.pae.dal.offer.interfaces.OfferDAO;
import be.vinci.pae.dal.offer.objects.OfferDAOImpl;
import be.vinci.pae.dal.services.interfaces.DALBackendService;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.dal.services.objects.DALServicesImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);
    bind(DALServicesImpl.class).to(DALBackendService.class).to(DALServices.class)
        .in(Singleton.class);

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

    //ItemsType
    bind(ItemsTypeUCCImpl.class).to(ItemsTypeUCC.class).in(Singleton.class);
    bind(ItemsTypeDAOImpl.class).to(ItemsTypeDAO.class).in(Singleton.class);
  }
}
