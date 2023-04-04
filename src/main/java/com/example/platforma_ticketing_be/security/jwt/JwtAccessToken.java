package com.example.platforma_ticketing_be.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.*;

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

  public UserDetail getUserDetail(){

    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(value).getBody();

    UserDetail userDetail = new UserDetail();
    userDetail.setUsername(claims.getSubject());
    userDetail.setToken(getValue());
    //here other info about user from token
    return userDetail;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    Map<String, Object> claims =  Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(value).getBody();

    //Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) claims.get("AUTHORITIES_KEY");
    List<Object> authorities = (List<Object>)claims.get("AUTHORITIES_KEY");

    //This method should be used in order to take the roles, but it didn't work
    //List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

    //This is just a workaround to take the roles
    List<String> roles = new ArrayList<>();
    authorities.forEach(authority -> roles.add(authority.toString().split("=")[1].replace("}", "")));

    List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
    roles.forEach(role -> simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role)));
    return simpleGrantedAuthorities;
  }
}
