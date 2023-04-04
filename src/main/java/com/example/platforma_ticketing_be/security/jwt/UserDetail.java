package com.example.platforma_ticketing_be.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDetail {

  private String username;
  private String token;
}
