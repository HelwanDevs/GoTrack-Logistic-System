package com.gotrack.support_and_notifications_service.notification.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;
import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;

@Component
public class EmailNotificationStrategy implements NotificationChannelStrategy {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationStrategy.class);

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String GotrackEmail;

    public EmailNotificationStrategy(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public void send(NotificationCreated event) {
        System.out.println("Sending EMAIL to profile " + event.getProfileId() + ": " + event.getMessage());
        logger.info("Sending EMAIL to profile {}: {} using email: {} to {}", event.getProfileId(), event.getMessage(),
                GotrackEmail, event.getEmail());

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(event.getEmail());
            mail.setFrom(GotrackEmail);

            mail.setSubject("Gotrack");
            mail.setText(event.getMessage());
            mailSender.send(mail);
            System.out.println("Email sent successfully");
            logger.info("Email sent successfully");

        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            logger.info("Failed to send email: {}", e.getMessage());

        }

    }
}
