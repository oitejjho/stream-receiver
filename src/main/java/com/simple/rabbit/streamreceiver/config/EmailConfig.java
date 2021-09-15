package com.simple.rabbit.streamreceiver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ConfigurationProperties("mail")
@Getter
@Setter
public class EmailConfig {

    private String host;

    private int port;

    private String timeout;

    private String connectionTimeout;

    private String subject;

    private String body;

    private String sender;

    private String receivers;

    @Bean
    public JavaMailSenderImpl smtpMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        Properties prop = mailSender.getJavaMailProperties();
        prop.put("mail.smtp.timeout", timeout);
        prop.put("mail.smtp.connectiontimeout", connectionTimeout);
        prop.put("mail.debug", true);
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", "false");
        mailSender.setJavaMailProperties(prop);
        return mailSender;
    }

}
