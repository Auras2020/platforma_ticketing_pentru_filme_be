package com.example.platforma_ticketing_be.service;

import com.example.platforma_ticketing_be.entities.UserAccount;
import com.example.platforma_ticketing_be.repository.UserRepository;
import com.example.platforma_ticketing_be.security.jwt.JwtAccessToken;
import com.example.platforma_ticketing_be.security.jwt.JwtAuthentication;
import com.example.platforma_ticketing_be.security.jwt.JwtTokenValidator;
import com.example.platforma_ticketing_be.security.jwt.UserDetail;
import com.example.platforma_ticketing_be.service.email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthentificationService {

    @Value("${platforma.ticketing.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private final JwtTokenValidator jwtTokenValidator;

    private final UserRepository userRepository;

    private final UserService userService;

    private final EmailServiceImpl emailService;


    public AuthentificationService(JwtTokenValidator jwtTokenValidator, UserRepository userRepository, UserService userService, EmailServiceImpl emailService) {
        this.jwtTokenValidator = jwtTokenValidator;
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    private String createAuthentication(String username, String password, boolean rememberMe, String role){
        UserAccount userAccount = this.userRepository.findByEmailAndPassword(username, password);

        Map<String, Object> claims = new HashMap<>();
        claims.put("AUTHORITIES_KEY", new SimpleGrantedAuthority(role));

        String token = jwtTokenValidator.generateJwtToken(userAccount, rememberMe, claims);
        JwtAccessToken accessToken = new JwtAccessToken(token, jwtSecret);
        JwtAuthentication authentication = new JwtAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return token;
    }

    public UserDetail login(String username, String password, boolean rememberMe) {
        UserAccount userAccount = this.userRepository.findByEmailAndPassword(username, password);
            String token = createAuthentication(username, password, rememberMe, userAccount.getRole());
            JwtAccessToken accessToken = new JwtAccessToken(token, jwtSecret);
            JwtAuthentication authentication = new JwtAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        userAccount.setToken(token);
        this.userRepository.save(userAccount);
        return (UserDetail) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public void resetPassword(String subject, String body, String email, String newPassword){
        UserAccount userAccount = this.userRepository.findByEmail(email);
        userAccount.setPassword(newPassword);
        this.userRepository.save(userAccount);
        this.emailService.sendEmail(subject, body, email);
    }

    public UserAccount checkIfAccountExists(String email, String password){
        return this.userRepository.findByEmailAndPassword(email, password);
    }

    public UserAccount checkIfEmailExists(String email){
        return this.userRepository.findByEmail(email);
    }

    /*private boolean checkCredentials(String userEmail, String userPass) {
        return email.equals(userEmail) && password.equals(userPass);
    }*/
}
