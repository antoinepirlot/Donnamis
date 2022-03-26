package be.vinci.pae.biz.interest.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.interest.interfaces.InterestUCC;
import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.interfaces.MemberUCC;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.dal.interest.interfaces.InterestDAO;
import be.vinci.pae.utils.ApplicationBinder;
import java.time.LocalDate;
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
  private final MemberUCC memberUCC = locator.getService(MemberUCC.class);
  private final MemberDTO memberWithPhoneNumber = new MemberImpl();
  private final MemberDTO memberWithoutPhoneNumber = new MemberImpl();
  private final MemberDTO memberNotExisting = new MemberImpl();
  private final int existingIdOffer = 5;
  private final int notExistingIdOffer = 5546;
  private final LocalDate date = LocalDate.now();


  @BeforeEach
  void setUp() {
    this.memberWithPhoneNumber.setId(1);
    this.memberWithPhoneNumber.setPhoneNumber("0458694778");
    this.memberWithoutPhoneNumber.setId(2);
    this.memberNotExisting.setId(646664);
    this.setMockitos();
  }

  private void setMockitos() {
    Mockito.when(this.interestDAO
            .markInterest(this.memberWithPhoneNumber, this.existingIdOffer, false, this.date))
        .thenReturn(1);
    Mockito.when(this.interestDAO
            .markInterest(this.memberWithPhoneNumber, this.existingIdOffer, true, this.date))
        .thenReturn(1);
    Mockito.when(this.interestDAO
            .markInterest(this.memberWithoutPhoneNumber, this.existingIdOffer, false, this.date))
        .thenReturn(1);
    Mockito.when(this.interestDAO
            .markInterest(this.memberWithoutPhoneNumber, this.existingIdOffer, true, this.date))
        .thenReturn(1);
    Mockito.when(this.memberUCC
            .getOneMember(this.memberWithPhoneNumber.getId()))
        .thenReturn(this.memberWithPhoneNumber);
    Mockito.when(this.memberUCC
            .getOneMember(this.memberWithoutPhoneNumber.getId()))
        .thenReturn(this.memberWithoutPhoneNumber);
    Mockito.when(this.interestDAO.offerExist(this.existingIdOffer)).thenReturn(true);
    Mockito.when(this.interestDAO.offerExist(this.notExistingIdOffer)).thenReturn(false);
    Mockito.when(this.interestDAO.memberExist(this.memberWithoutPhoneNumber)).thenReturn(true);
    Mockito.when(this.interestDAO.memberExist(this.memberNotExisting)).thenReturn(false);
    Mockito.when(this.interestDAO
            .interestExist(this.existingIdOffer, this.memberWithoutPhoneNumber))
        .thenReturn(true);
    Mockito.when(this.interestDAO
            .interestExist(this.existingIdOffer, this.memberNotExisting))
        .thenReturn(false);
    Mockito.when(this.interestDAO
            .interestExist(this.notExistingIdOffer, this.memberWithoutPhoneNumber))
        .thenReturn(false);
    Mockito.when(this.interestDAO
            .interestExist(this.notExistingIdOffer, this.memberNotExisting))
        .thenReturn(false);
  }

  @DisplayName("Test mark interst with phone number and no call wanted")
  @Test
  void testMarkInterestWithPhoneNumberAndNoCallWanted() {
    assertEquals(
        1,
        this.interestUCC.markInterest(this.memberWithPhoneNumber, this.existingIdOffer, false)
    );
  }

  @DisplayName("Test mark interest with phone number and call wanted")
  @Test
  void testMarkInterestWithPhoneNumberAndCallWanted() {
    assertEquals(
        1,
        this.interestUCC.markInterest(this.memberWithPhoneNumber, this.existingIdOffer, true)
    );
  }

  @DisplayName("Test mark interest without phone number and no call wanted")
  @Test
  void testMarkInterestWithoutPhoneNumberAndNoCallWanted() {
    assertEquals(
        1,
        this.interestUCC.markInterest(this.memberWithoutPhoneNumber, this.existingIdOffer, false)
    );
  }

  @DisplayName("Test mark interest without phone number and call wanted")
  @Test
  void testMarkInterestWithoutPhoneNumberAndCallWanted() {
    assertEquals(
        0,
        this.interestUCC.markInterest(this.memberWithoutPhoneNumber, this.existingIdOffer, true)
    );
  }

  @DisplayName("Test offer exist with existing id offer")
  @Test
  void testOfferExistWithExistingIdOffer() {
    assertTrue(this.interestUCC.offerExist(this.existingIdOffer));
  }

  @DisplayName("Test offer not exist with not existing id offer")
  @Test
  void testOfferNotExistsWithExistingIdOffer() {
    assertFalse(this.interestUCC.offerExist(this.notExistingIdOffer));
  }

  @DisplayName("Test member exist with existing id member")
  @Test
  void testMemberExistWithExistingIdMember() {
    assertTrue(this.interestUCC.memberExist(this.memberWithoutPhoneNumber));
  }

  @DisplayName("Test member not exist with not existing id member")
  @Test
  void testMemberNotExistsWithExistingIdMember() {
    assertFalse(this.interestUCC.memberExist(this.memberNotExisting));
  }

  @DisplayName("Test interest exists with existing offer id and existing member")
  @Test
  void testInterestExistsWithExistingOfferIdAndExistingMember() {
    assertTrue(this.interestUCC.interestExist(this.existingIdOffer, this.memberWithoutPhoneNumber));
  }

  @DisplayName("Test interest exists with not existing offer id and existing member")
  @Test
  void testInterestExistsWithNotExistingOfferIdAndExistingMember() {
    assertFalse(this.interestUCC
        .interestExist(this.notExistingIdOffer, this.memberWithoutPhoneNumber)
    );
  }

  @DisplayName("Test interest exists with existing offer id and not existing member")
  @Test
  void testInterestExistsWithExistingOfferIdAndNotExistingMember() {
    assertFalse(this.interestUCC.interestExist(this.existingIdOffer, this.memberNotExisting));
  }

  @DisplayName("Test interest exists with not existing offer id and not existing member")
  @Test
  void testInterestExistsWithNotExistingOfferIdAndNotExistingMember() {
    assertFalse(this.interestUCC.interestExist(this.notExistingIdOffer, this.memberNotExisting));
  }
}