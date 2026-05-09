package com.gotrack.support_and_notifications_service.notification.strategy;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;
import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;

public interface NotificationChannelStrategy {

    NotificationChannel channel();

    void send(NotificationCreated event);

}
