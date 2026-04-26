package com.gotrack.support_and_notifications_service.notification.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintSubmit {

    private final String complaintId;
    private final String subject;
    private final String profileId;
    private final long shipmentId;

    public ComplaintSubmit(String complaintId, String profileId, String subject, long shipmentId) {
        this.complaintId = complaintId;
        this.subject = subject;
        this.profileId = profileId;
        this.shipmentId = shipmentId;
    }

}
