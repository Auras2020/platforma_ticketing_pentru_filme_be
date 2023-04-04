package com.example.platforma_ticketing_be.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Value("${platforma.ticketing.email}")
    private String mailFrom;

    //@Value("${platforma.ticketing.email}")
    //private String mailTo;

    @Override
    public void sendEmail(String subject, String body, String mailTo) {
        System.out.println(body);
        System.out.println(subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(mailTo);
        message.setSubject(subject);
        //body += "<a href='http://localhost:4200/reset-password'>Click</a>";
        message.setText(body);
        mailSender.send(message);
    }

}
