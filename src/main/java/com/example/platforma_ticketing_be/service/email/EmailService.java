package com.example.platforma_ticketing_be.service.email;

public interface EmailService {

    void sendEmail(String body, String subject, String mailTo);
}
