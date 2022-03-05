package be.vinci.pae.utils;

import be.vinci.pae.biz.Factory;
import be.vinci.pae.biz.FactoryImpl;
import be.vinci.pae.biz.MemberUCC;
import be.vinci.pae.biz.MemberUCCImpl;
import be.vinci.pae.dal.MemberDAO;
import be.vinci.pae.dal.MemberDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(Mockito.mock(MemberDAOImpl.class)).to(MemberDAO.class);
  }
}
