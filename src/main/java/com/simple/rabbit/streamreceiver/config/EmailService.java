package com.simple.rabbit.streamreceiver.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSenderImpl smtpMailSender;

    public Future<MimeMessage> sendEmail(String sender, String[] receivers, String subject, String body, List<File> attachments) throws MessagingException {

        MimeMessage mimeMessage = smtpMailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(sender);
            helper.setTo(receivers);
            helper.setText(body);
            helper.setSubject(subject);
            for (File attachment : attachments) {
                helper.addAttachment(attachment.getName(), new FileSystemResource(attachment));
            }
            smtpMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            log.info("Failed sending email with error : {}, {}, {}", e.getMessage(), e.getCause(), e);
            throw e;
        }

        return new AsyncResult<>(mimeMessage);
    }
}
