package com.gotrack.support_and_notifications_service.notification.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;

@Component
public class NotificationListener {// this class is for demonstration purposes, to show how to listen to events. In
                                   // a real application, you would have more complex logic here, such as sending
                                   // emails or SMS messages.

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String GotrackEmail;

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @Async
    @EventListener
    public void handleNotificationCreatedEvent(NotificationCreated event) {

        switch (event.getChannel()) {
            case SMS:
                sendsms(event);
                break;
            case EMAIL:
                sendemail(event);
                break;
            case IN_APP:
                sendinapp(event);
                break;
            case WHATSAPP:
                sendwhatsapp(event);
                break;
            default:
                System.out.println("Unknown channel: " + event.getChannel());
        }
    }

    private void sendsms(NotificationCreated event) {
        System.out.println("Sending SMS to profile " + event.getProfileId() + ": " + event.getMessage());
        log.info("Sending SMS to profile {}: {}", event.getProfileId(), event.getMessage());
    }

    private void sendemail(NotificationCreated event) {
        System.out.println("Sending EMAIL to profile " + event.getProfileId() + ": " + event.getMessage());
        log.info("Sending EMAIL to profile {}: {} using email: {} to {}", event.getProfileId(), event.getMessage(),
                GotrackEmail, event.getEmail());

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(event.getEmail());
            mail.setFrom(GotrackEmail);

            mail.setSubject("Gotrack");
            mail.setText(event.getMessage());
            mailSender.send(mail);
            System.out.println("Email sent successfully");
            log.info("Email sent successfully");

        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            log.info("Failed to send email: {}", e.getMessage());

        }
    }

    private void sendinapp(NotificationCreated event) {
        System.out
                .println("Sending IN_APP notification to profile " + event.getProfileId() + ": " + event.getMessage());
        log.info("Sending IN_APP to profile {}: {}", event.getProfileId(), event.getMessage());

        // display by getting from db using GET
    }

    private void sendwhatsapp(NotificationCreated event) {
        System.out.println("Sending WHATSAPP message to profile " + event.getProfileId() + ": " + event.getMessage());
        log.info("Sending WHATSAPP to profile {}: {}", event.getProfileId(), event.getMessage());

    }

}
