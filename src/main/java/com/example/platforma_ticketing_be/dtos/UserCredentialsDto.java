package com.example.platforma_ticketing_be.dtos;

public class UserCredentialsDto {

    private String email;

    private String password;

    private Boolean rememberMe;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

}
