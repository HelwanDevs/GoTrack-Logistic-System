package com.gotrack.support_and_notifications_service.notification.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;
import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;

@Component
public class WhatsNotificationStrategy implements NotificationChannelStrategy {

    private static final Logger logger = LoggerFactory.getLogger(WhatsNotificationStrategy.class);

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.WHATSAPP;
    }

    @Override
    public void send(NotificationCreated event) {
        System.out.println("Sending WhatsApp message to profile " + event.getProfileId() + ": " + event.getMessage());
        logger.info("Sending WhatsApp message to profile {}: {}", event.getProfileId(), event.getMessage());

    }
}
