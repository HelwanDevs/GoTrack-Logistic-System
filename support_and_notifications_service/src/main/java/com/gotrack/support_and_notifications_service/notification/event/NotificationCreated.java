package com.gotrack.support_and_notifications_service.notification.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCreated {

    private final String NotificationId;
    private final long profileId;
    private final String message;
    private final String channel;

    public NotificationCreated(String notificationId, long profileId, String message, String channel) {
        this.NotificationId = notificationId;
        this.profileId = profileId;
        this.message = message;
        this.channel = channel;
    }

}
