package com.example.platforma_ticketing_be.service.email;

import javax.activation.DataSource;
import java.util.List;

public interface EmailService {

    void sendEmail(String body, String subject, String mailTo);

    void processEmailWithAttachments(String body, String subject, String mailTo, List<DataSource> attachments);
}
