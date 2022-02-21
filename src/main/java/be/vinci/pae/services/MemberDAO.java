package be.vinci.pae.services;

import be.vinci.pae.domain.Member;
import be.vinci.pae.services.utils.Json;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class MemberDAO {

  private static final String COLLECTION_NAME = "members";
  private static Json<Member> jsonDB = new Json<>();

  public List<Member> getAll() {
    List<Member> members = jsonDB.parse(COLLECTION_NAME);
    return members;
  }

  public Member createOne(Member member) {
    List<Member> members = jsonDB.parse(COLLECTION_NAME);
    member.setId(nextMemberId());
    member.setUsername(StringEscapeUtils.escapeHtml4(member.getUsername()));
    member.setPassword(StringEscapeUtils.escapeHtml4(member.getPassword()));
    member.setLastName(StringEscapeUtils.escapeHtml4(member.getLastName()));
    member.setFirstName(StringEscapeUtils.escapeHtml4(member.getFirstName()));
    member.setActualState(StringEscapeUtils.escapeHtml4(member.getActualState()));
    member.setPhoneNumber(StringEscapeUtils.escapeHtml4(member.getPhoneNumber()));
    member.setAdmin(member.isAdmin());

    members.add(member);
    jsonDB.serialize(members, COLLECTION_NAME);
    return member;
  }

  public int nextMemberId() {
    List<Member> members = jsonDB.parse(COLLECTION_NAME);
    ;
    if (members.size() == 0) {
      return 1;
    }
    return members.get(members.size() - 1).getId() + 1;
  }
}

