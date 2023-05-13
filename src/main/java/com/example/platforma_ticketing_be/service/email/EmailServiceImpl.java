package com.example.platforma_ticketing_be.service.email;

import org.flywaydb.core.internal.resource.filesystem.FileSystemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

   /* @PostConstruct
    public void sim(){
        sendEmailWithAttachment("hello", "tickets", "andreipop767@gmail.com", "C:/Users/Ovreiu.Au.Auras/Desktop/Lab_09-13.pdf");
    }*/

    @Override
    public void processEmailWithAttachments(String body, String subject, String mailTo, List<DataSource> attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(mailFrom);
            helper.setTo(mailTo);
            helper.setSubject(subject);
            helper.setText(body);

            //for(int i = 0; i < attachments.size(); i++){
                helper.addAttachment("Tickets.pdf", attachments.get(0));
            //}

            mailSender.send(message);

            System.out.println("Email sent successfully with attachment.");
        } catch (MessagingException e) {
            System.err.println("Failed to send email with attachment: " + e.getMessage());
        }
    }

}
