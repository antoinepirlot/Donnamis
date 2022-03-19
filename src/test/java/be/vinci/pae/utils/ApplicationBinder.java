package be.vinci.pae.utils;

import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.member.objects.MemberUCCImpl;
import dal.member.interfaces.MemberDAO;
import dal.member.objects.MemberDAOImpl;
import jakarta.inject.Singleton;
import jakarta.ws.rs.ext.Provider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;

@Provider
public class ApplicationBinder extends AbstractBinder {

  @Override
  protected void configure() {
    bind(Mockito.mock(MemberDAOImpl.class)).to(MemberDAO.class);
    bind(MemberUCCImpl.class).to(MemberUCC.class).in(Singleton.class);
  }
}
