package com.gotrack.support_and_notifications_service.notification.event;

import java.util.List;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ComplaintSubmit {

    private final String complaintId;
    private final String profileId;
    private final String email;
    private final String subject;
    private final Long shipmentId;
    private final List<NotificationChannel> channel;

}
