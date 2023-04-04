package com.example.platforma_ticketing_be.security.jwt;

import com.example.platforma_ticketing_be.entities.UserAccount;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenValidator {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenValidator.class);

    @Value("${platforma.ticketing.jwtSecret}")
    private String jwtSecret;

    @Value("${platforma.ticketing.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${platforma.ticketing.jwtExpirationMsRememberMe}")
    private long jwtExpirationMsRememberMe;

    public String generateJwtToken(UserAccount userAccount, boolean rememberMe,
                                   Map<String, Object> claims) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userAccount.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + (rememberMe ? jwtExpirationMsRememberMe : jwtExpirationMs)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;

        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
