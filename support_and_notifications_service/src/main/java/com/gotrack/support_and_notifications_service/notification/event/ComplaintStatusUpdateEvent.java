package com.gotrack.support_and_notifications_service.notification.event;

import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintStatusUpdateEvent {
    private final String complaintId;
    private final String profileId;
    private final ComplaintStatus status;
    private final String note;

    public ComplaintStatusUpdateEvent(String complaintId, String profileId, ComplaintStatus status, String note) {
        this.complaintId = complaintId;
        this.profileId = profileId;
        this.status = status;
        this.note = note;
    }

}
