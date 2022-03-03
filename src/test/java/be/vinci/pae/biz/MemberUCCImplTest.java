package be.vinci.pae.biz;

import org.junit.jupiter.api.BeforeEach;

class MemberUCCImplTest {

  private Factory factory = new FactoryImpl();
  private Member member;

  @BeforeEach
  void setUp() {
    member = factory.getMember();
    member.setId(1);
    member.setUsername("MemberTest");
    member.setPassword("$2a$10$Psvm5269Z5F.eR1LRz92feZ/s1Euxsi15yBkLRFWJkX0PGhidAFP6");
    member.setLastName("Beta");
    member.setFirstName("Alpha");
    member.setAdmin(false);
    member.setActualState("confirmed");
    member.setPhoneNumber("0477810590");
  }

}