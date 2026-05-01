package com.gotrack.support_and_notifications_service.notification.event;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationCreated {

    private final String notificationId;
    private final String profileId;
    private final String message;
    private final NotificationChannel channel;
    private final String email;

}
