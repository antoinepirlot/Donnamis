package be.vinci.pae;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.MemberUCC;
import be.vinci.pae.biz.MemberUCCImpl;
import be.vinci.pae.dal.MemberDAO;
import be.vinci.pae.utils.ApplicationBinder;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DemoTest {

  private MemberDAO memberDAO;
  private MemberUCC memberUCC;

  @BeforeEach
  void init() {
    ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
    this.memberDAO = locator.getService(MemberDAO.class);
    this.memberUCC = new MemberUCCImpl();
  }

  @Test
  public void demoTest() {
    assertNotNull(this.memberDAO);
  }

}
