package com.gotrack.support_and_notifications_service.notification.event;

import java.util.List;

import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;
import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ComplaintStatusUpdateEvent {
    private final String complaintId;
    private final String profileId;
    private final String email;
    private final ComplaintStatus status;
    private final String note;
    private final List<NotificationChannel> channel;

}
