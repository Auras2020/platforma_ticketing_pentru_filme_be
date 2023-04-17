package com.example.platforma_ticketing_be.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.platforma_ticketing_be.security.InvalidTokenException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


public class JwtAccessToken {

  private String value;

  private String jwtSecret;

  public JwtAccessToken(String value, String jwtSecret){
    this.value = value;
    this.jwtSecret= jwtSecret;
  }

  public String getUsername() {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(value).getBody().getSubject();
  }

  public Date getExpirationDate() {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(value).getBody().getExpiration();
  }

  public String getValue() {
    return value;
  }

  private String getRole(JsonObject payloadAsJson){
    return payloadAsJson.getAsJsonObject("AUTHORITIES_KEY").get("authority").getAsString();

  }

  private JsonObject getPayloadAsJsonObject() {
    DecodedJWT decodedJWT = decodeToken(value);
    return decodeTokenPayloadToJsonObject(decodedJWT);
  }

  private DecodedJWT decodeToken(String value) {
    if (value == null) {
      throw new InvalidTokenException("Token has not been provided");
    }
    return JWT.decode(value);
  }

  private JsonObject decodeTokenPayloadToJsonObject(DecodedJWT decodedJWT) {
    try {
      String payloadAsString = decodedJWT.getPayload();
      return new Gson().fromJson(
              new String(Base64.getDecoder().decode(payloadAsString), StandardCharsets.UTF_8),
              JsonObject.class);
    } catch (RuntimeException exception) {
      throw new InvalidTokenException("Invalid JWT or JSON format of each of the jwt parts", exception);
    }
  }

  public UserDetail getUserDetail(){

    JsonObject payloadAsJson = getPayloadAsJsonObject();

    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(value).getBody();

    UserDetail userDetail = new UserDetail();
    userDetail.setUsername(claims.getSubject());
    userDetail.setToken(getValue());
    userDetail.setRole(getRole(payloadAsJson));
    //here other info about user from token
    return userDetail;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    JsonObject payloadAsJson = getPayloadAsJsonObject();
    return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + getRole(payloadAsJson)));
  }
}
