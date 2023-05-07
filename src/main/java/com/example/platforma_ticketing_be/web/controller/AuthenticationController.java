package com.example.platforma_ticketing_be.web.controller;

import com.example.platforma_ticketing_be.dtos.EmailDto;
import com.example.platforma_ticketing_be.dtos.ResetPasswordDto;
import com.example.platforma_ticketing_be.dtos.UserCredentialsDto;
import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.security.jwt.UserDetail;
import com.example.platforma_ticketing_be.service.AuthentificationService;
import com.example.platforma_ticketing_be.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthentificationService authentificationService;
    private final EmailServiceImpl emailService;

    @Autowired
    public AuthenticationController(AuthentificationService authentificationService, EmailServiceImpl emailService) {
        this.authentificationService = authentificationService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public UserDetail login(@RequestBody UserCredentialsDto userCredentialsDto) {
        return authentificationService.login(userCredentialsDto.getEmail(),
                userCredentialsDto.getPassword(), userCredentialsDto.getRememberMe());
    }

    @PostMapping("/logout-user")
    public void logout() {
        authentificationService.logout();
    }

    @PostMapping("/forgot-password")
    public void sendForgotPasswordEmail(@RequestBody EmailDto emailDto){
        this.emailService.sendEmail(emailDto.getSubject(), emailDto.getBody(), emailDto.getEmail());
    }

    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordDto resetPasswordDto){
        this.authentificationService.resetPassword(resetPasswordDto.getSubject(), resetPasswordDto.getBody(),
                resetPasswordDto.getEmail(), resetPasswordDto.getPassword());
    }

    @PostMapping("/find-account")
    public UserAccount checkIfAccountExists(@RequestBody UserCredentialsDto userCredentialsDto){
        return this.authentificationService.checkIfAccountExists(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());
    }

    @GetMapping("/find-email/{email}")
    public UserAccount checkIfEmailExists(@PathVariable("email") String email){
        return this.authentificationService.checkIfEmailExists(email);
    }
}
