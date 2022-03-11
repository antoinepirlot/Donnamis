package be.vinci.pae.utils;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.FactoryImpl;
import be.vinci.pae.biz.MemberUCC;
import be.vinci.pae.biz.MemberUCCImpl;
import be.vinci.pae.dal.DALServices;
import be.vinci.pae.dal.DALServicesImpl;
import be.vinci.pae.dal.MemberDAO;
import be.vinci.pae.dal.MemberDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(DALServicesImpl.class).to(DALServices.class).in(Singleton.class);
    bind(FactoryImpl.class).to(Factory.class).in(Singleton.class);
    bind(MemberDAOImpl.class).to(MemberDAO.class).in(Singleton.class);
    bind(MemberUCCImpl.class).to(MemberUCC.class).in(Singleton.class);
  }
}
