package com.gotrack.support_and_notifications_service.notification.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;
import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;

@Component
public class InAppNotificationStrategy implements NotificationChannelStrategy {

    private static final Logger logger = LoggerFactory.getLogger(InAppNotificationStrategy.class);

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.IN_APP;
    }

    @Override
    public void send(NotificationCreated event) {
        System.out
                .println("Sending IN_APP notification to profile " + event.getProfileId() + ": " + event.getMessage());
        logger.info("Sending IN_APP to profile {}: {}", event.getProfileId(), event.getMessage());
        // display by getting from db using GET

    }
}
