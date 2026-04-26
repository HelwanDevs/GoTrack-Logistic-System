package com.gotrack.support_and_notifications_service.notification.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;

@Component
public class NotificationListener {// this class is for demonstration purposes, to show how to listen to events. In
                                   // a real application, you would have more complex logic here, such as sending
                                   // emails or SMS messages.

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @Async
    @EventListener
    public void handleNotificationCreatedEvent(NotificationCreated event) {

        switch (event.getChannel()) {
            case "SMS":
                sendsms(event);
                break;
            case "EMAIL":
                sendemail(event);
                break;
            default:
                log.warn("Unknown notification channel: " + event.getChannel());
        }
    }

    private void sendsms(NotificationCreated event) {
        System.out.println("Sending SMS to profile " + event.getProfileId() + ": " + event.getMessage());
    }

    private void sendemail(NotificationCreated event) {
        System.out.println("Sending EMAIL to profile " + event.getProfileId() + ": " + event.getMessage());
    }

}
