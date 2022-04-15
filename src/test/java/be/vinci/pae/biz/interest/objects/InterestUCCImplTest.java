package be.vinci.pae.biz.interest.objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.interest.interfaces.InterestDTO;
import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.biz.offer.interfaces.OfferDTO;
import be.vinci.pae.biz.offer.objects.OfferImpl;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InterestUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());
  private final InterestDAO interestDAO = locator.getService(InterestDAO.class);
  private final InterestUCC interestUCC = locator.getService(InterestUCC.class);
  private final DALServices dalBackendService = locator.getService(DALServices.class);
  private final MemberDTO memberWithPhoneNumber = new MemberImpl();
  private final MemberDTO memberWithoutPhoneNumber = new MemberImpl();
  private final MemberDTO memberNotExisting = new MemberImpl();
  private final OfferDTO existingOffer = new OfferImpl();
  private final OfferDTO notExistingOffer = new OfferImpl();
  private final InterestDTO notExistingInterestWithExistingMemberAndOffer = new InterestImpl();
  private final InterestDTO notExistingInterestWithNotExistingMember = new InterestImpl();
  private final InterestDTO notExistingInterestWithNotExistingOffer = new InterestImpl();
  private final InterestDTO notExistingInterestWithNotExistingMemberAndOffer = new InterestImpl();
  private final InterestDTO existingInterestWithExistingMemberAndOffer = new InterestImpl();
  private final InterestDTO existingInterestWithNotExistingMember = new InterestImpl();
  private final InterestDTO existingInterestWithNotExistingOffer = new InterestImpl();
  private final InterestDTO existingInterestWithNotExistingMemberAndOffer = new InterestImpl();
  private final Timestamp date = Timestamp.valueOf(LocalDateTime.now());


  @BeforeEach
  void setUp() throws SQLException {
    this.memberWithPhoneNumber.setId(1);
    this.memberWithPhoneNumber.setPhoneNumber("0458694778");
    this.memberWithoutPhoneNumber.setId(2);
    this.memberNotExisting.setId(646664);
    this.existingOffer.setId(5);
    this.notExistingOffer.setId(5546);
    this.notExistingInterestWithExistingMemberAndOffer.setMember(this.memberWithoutPhoneNumber);
    this.notExistingInterestWithExistingMemberAndOffer.setOffer(this.existingOffer);

    this.existingInterestWithExistingMemberAndOffer.setId(5);
    this.existingInterestWithExistingMemberAndOffer.setMember(this.memberWithoutPhoneNumber);
    this.existingInterestWithExistingMemberAndOffer.setOffer(this.existingOffer);
    this.setMockitos();
  }

  private void setMockitos() throws SQLException {

    Mockito.when(this.interestDAO.markInterest(this.notExistingInterestWithExistingMemberAndOffer))
        .thenReturn(true);
    Mockito.when(this.interestDAO.markInterest(this.existingInterestWithExistingMemberAndOffer))
        .thenReturn(false);

    Mockito.when(this.interestDAO.markInterest(this.notExistingInterestWithNotExistingMember))
        .thenReturn(true);
    Mockito.when(this.interestDAO.markInterest(this.existingInterestWithNotExistingMember))
        .thenReturn(false);

    Mockito.when(this.interestDAO.markInterest(this.notExistingInterestWithNotExistingOffer))
        .thenReturn(true);
    Mockito.when(this.interestDAO.markInterest(this.existingInterestWithNotExistingOffer))
        .thenReturn(false);

    Mockito.when(this.interestDAO
            .markInterest(this.notExistingInterestWithNotExistingMemberAndOffer))
        .thenReturn(true);
    Mockito.when(this.interestDAO.markInterest(this.existingInterestWithNotExistingMemberAndOffer))
        .thenReturn(false);

    Mockito.when(this.interestDAO
            .interestExist(this.existingInterestWithExistingMemberAndOffer))
        .thenReturn(true);
    Mockito.when(this.interestDAO
            .interestExist(this.notExistingInterestWithExistingMemberAndOffer))
        .thenReturn(false);
  }

  @DisplayName("Test mark interest with phone number and no call wanted")
  @Test
  void testMarkInterestWithPhoneNumberAndNoCallWanted() throws SQLException {
    System.out.println(
        this.interestDAO.markInterest(this.notExistingInterestWithExistingMemberAndOffer));
    assertTrue(
        this.interestUCC.markInterest(this.notExistingInterestWithExistingMemberAndOffer)
    );
  }

  @DisplayName("Test mark interest with phone number and call wanted")
  @Test
  void testMarkInterestWithPhoneNumberAndCallWanted() throws SQLException {
    assertTrue(
        this.interestUCC.markInterest(this.notExistingInterestWithExistingMemberAndOffer)
    );
  }

  @DisplayName("Test mark interest without phone number and no call wanted")
  @Test
  void testMarkInterestWithoutPhoneNumberAndNoCallWanted() throws SQLException {
    assertTrue(
        this.interestUCC.markInterest(this.notExistingInterestWithExistingMemberAndOffer)
    );
  }

  @DisplayName("Test mark interest without phone number and call wanted")
  @Test
  void testMarkInterestWithoutPhoneNumberAndCallWanted() throws SQLException {
    assertTrue(
        this.interestUCC.markInterest(this.notExistingInterestWithExistingMemberAndOffer)
    );
  }

  @DisplayName("Test interest exists with existing offer id and existing member")
  @Test
  void testInterestExistsWithExistingOfferIdAndExistingMember() throws SQLException {
    assertTrue(this.interestUCC.interestExist(this.existingInterestWithExistingMemberAndOffer));
  }

  @DisplayName("Test interest exists with not existing offer id and existing member")
  @Test
  void testInterestExistsWithNotExistingOfferIdAndExistingMember() throws SQLException {
    assertFalse(this.interestUCC
        .interestExist(this.notExistingInterestWithNotExistingOffer)
    );
  }

  @DisplayName("Test interest exists with existing offer id and not existing member")
  @Test
  void testInterestExistsWithExistingOfferIdAndNotExistingMember() throws SQLException {
    assertFalse(
        this.interestUCC.interestExist(this.notExistingInterestWithNotExistingMember)
    );
  }

  @DisplayName("Test interest exists with not existing offer id and not existing member")
  @Test
  void testInterestExistsWithNotExistingOfferIdAndNotExistingMember() throws SQLException {
    assertFalse(
        this.interestUCC.interestExist(this.notExistingInterestWithNotExistingMemberAndOffer)
    );
  }
}