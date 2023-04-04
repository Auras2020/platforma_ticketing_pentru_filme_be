package com.example.platforma_ticketing_be.security.jwt;

import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ToString
public class JwtAuthentication extends UsernamePasswordAuthenticationToken {

  private final JwtAccessToken accessToken;


  public JwtAuthentication(JwtAccessToken accessToken) {
    super(accessToken.getUsername(), null, accessToken.getAuthorities());
    this.accessToken = accessToken;
  }

  @Override
  public Object getCredentials() {
    return accessToken.getValue();
  }

  @Override
  public Object getPrincipal() {
    return accessToken.getUsername();
  }

  @Override public Object getDetails() {
    return accessToken.getUserDetail();
  }
}
