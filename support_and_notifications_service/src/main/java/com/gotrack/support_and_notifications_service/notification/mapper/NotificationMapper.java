package com.gotrack.support_and_notifications_service.notification.mapper;

import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.notification.entity.Notifications;

@Component
public class NotificationMapper {

    public Notifications toNotificationMessage(String profileId, String message) {

        return Notifications.builder()
                .profileId(profileId)
                .message(message)
                .channel("SMS")
                .isRead(false)
                .build();
    }

}
