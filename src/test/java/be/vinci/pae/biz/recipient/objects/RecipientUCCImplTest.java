package be.vinci.pae.biz.recipient.objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import be.vinci.pae.biz.member.interfaces.MemberDTO;
import be.vinci.pae.biz.member.objects.MemberImpl;
import be.vinci.pae.biz.recipient.interfaces.RecipientDTO;
import be.vinci.pae.biz.recipient.interfaces.RecipientUCC;
import be.vinci.pae.dal.member.interfaces.MemberDAO;
import be.vinci.pae.dal.recipient.interfaces.RecipientDAO;
import be.vinci.pae.dal.services.interfaces.DALServices;
import be.vinci.pae.exceptions.FatalException;
import be.vinci.pae.utils.ApplicationBinder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RecipientUCCImplTest {

  private final ServiceLocator locator = ServiceLocatorUtilities.bind(new ApplicationBinder());

  private final RecipientDAO recipientDAO = locator.getService(RecipientDAO.class);

  private final MemberDAO memberDAO = locator.getService(MemberDAO.class);

  private final DALServices dalServices = locator.getService(DALServices.class);

  private final RecipientUCC recipientUCC = locator.getService(RecipientUCC.class);

  private final RecipientDTO recipientDTO = new RecipientImpl();

  private final List<RecipientDTO> ratingDTOList = new ArrayList<>();


  @BeforeEach
  void setUp() {
    MemberDTO memberDTO = new MemberImpl();
    this.recipientDTO.setMember(memberDTO);
    try {
      Mockito.doNothing().when(this.dalServices).start();
      Mockito.doNothing().when(this.dalServices).commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void setChooseRecipientReturnedValue(boolean chosen) {
    Mockito.when(
        this.memberDAO.memberExist(this.recipientDTO.getMember(), -1)
    ).thenReturn(true);
    Mockito.when(this.memberDAO.getOne(this.recipientDTO.getMember().getId()))
        .thenReturn(this.recipientDTO.getMember());
    Mockito.when(this.recipientDAO.chooseRecipient(this.recipientDTO)).thenReturn(chosen);
  }

  private void setExistsReturnedValue(boolean exists) {
    Mockito.when(this.recipientDAO.exists(this.recipientDTO)).thenReturn(exists);
  }

  private void setErrorDALServiceStart() {
    try {
      Mockito.doThrow(new SQLException()).when(dalServices).start();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void setErrorDALServiceCommit() {
    try {
      Mockito.doThrow(new SQLException()).when(dalServices).commit();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @DisplayName("Test choose recipient working as expected")
  @Test
  void testChooseRecipientWorkingAsExpected() {
    this.setChooseRecipientReturnedValue(true);
    Mockito.when(this.memberDAO.getOne(this.recipientDTO.getMember()))
        .thenReturn(this.recipientDTO.getMember());
    this.recipientDTO.getMember().setActualState("confirmed");
    assertDoesNotThrow(() -> this.recipientUCC.chooseRecipient(this.recipientDTO));
  }

  @DisplayName("Test choose recipient not chosen")
  @Test
  void testChooseRecipientNotChosen() {
    this.setChooseRecipientReturnedValue(false);
    Mockito.when(this.memberDAO.getOne(this.recipientDTO.getMember()))
        .thenReturn(this.recipientDTO.getMember());
    this.recipientDTO.getMember().setActualState("confirmed");
    assertThrows(FatalException.class, () -> this.recipientUCC.chooseRecipient(this.recipientDTO));
  }

  @DisplayName("Test choose recipient with start throwing sql exception")
  @Test
  void testChooseRecipientWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.recipientUCC.chooseRecipient(this.recipientDTO));
  }

  @DisplayName("Test choose recipient with commit throwing sql exception")
  @Test
  void testChooseRecipientWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.recipientUCC.chooseRecipient(this.recipientDTO));
  }

  @DisplayName("Test exists with existing recipient")
  @Test
  void testExistsWithExistingRecipient() {
    this.setExistsReturnedValue(true);
    assertTrue(this.recipientUCC.exists(this.recipientDTO));
  }

  @DisplayName("Test exists with not existing recipient")
  @Test
  void testExistsWithNotExistingRecipient() {
    this.setExistsReturnedValue(false);
    assertFalse(this.recipientUCC.exists(this.recipientDTO));
  }

  @DisplayName("Test exists with start throwing sql exception")
  @Test
  void testExistsWithStartThrowingSQLException() {
    this.setErrorDALServiceStart();
    assertThrows(FatalException.class, () -> this.recipientUCC.exists(this.recipientDTO));
  }

  @DisplayName("Test exists with commit throwing sql exception")
  @Test
  void testExistsWithCommitThrowingSQLException() {
    this.setErrorDALServiceCommit();
    assertThrows(FatalException.class, () -> this.recipientUCC.exists(this.recipientDTO));
  }
}