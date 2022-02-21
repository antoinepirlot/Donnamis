package be.vinci.pae.services;

import be.vinci.pae.domain.Member;
import be.vinci.pae.services.utils.Json;
import be.vinci.pae.utils.Config;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import org.apache.commons.text.StringEscapeUtils;

public class MemberDAO {

  private static final String COLLECTION_NAME = "members";
  private static Json<Member> jsonDB = new Json<>();
  private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
  private final ObjectMapper jsonMapper = new ObjectMapper();


  public List<Member> getAll() {
    List<Member> members = jsonDB.parse(COLLECTION_NAME);
    return members;
  }


  public Member getOne(int id) {
    var items = jsonDB.parse(COLLECTION_NAME);
    return items.stream().filter(item -> item.getId() == id).findAny().orElse(null);
  }

  public Member getOne(String membername) {
    var items = jsonDB.parse(COLLECTION_NAME);
    return items.stream().filter(item -> item.getUsername().equals(membername)).findAny()
        .orElse(null);
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

    members.add(member);
    jsonDB.serialize(members, COLLECTION_NAME);
    return member;
  }


  public ObjectNode login(String username, String password) {
    Member member = getOne(username);
    if (member == null || !member.checkPassword(password)) {
      return null;
    }
    String token;
    try {
      token = JWT.create().withIssuer("auth0")
          .withClaim("member", member.getId()).sign(this.jwtAlgorithm);
      ObjectNode publicMember = jsonMapper.createObjectNode()
          .put("token", token)
          .put("id", member.getId())
          .put("username", member.getUsername());
      return publicMember;
    } catch (Exception e) {
      System.out.println("Unable to create token");
      return null;
    }
  }

  public int nextMemberId() {
    List<Member> members = jsonDB.parse(COLLECTION_NAME);
    
    if (members.size() == 0) {
      return 1;
    }
    return members.get(members.size() - 1).getId() + 1;
  }
}

