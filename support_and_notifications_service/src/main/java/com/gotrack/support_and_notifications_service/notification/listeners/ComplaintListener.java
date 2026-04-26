package com.gotrack.support_and_notifications_service.notification.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.notification.event.ComplaintStatusUpdateEvent;
import com.gotrack.support_and_notifications_service.notification.event.ComplaintSubmit;
import com.gotrack.support_and_notifications_service.notification.service.NotificationService;

@Component
public class ComplaintListener {

    @Autowired
    private NotificationService service;

    @Async
    @EventListener
    public void handleComplaintSubmitEvent(ComplaintSubmit event) {
        String message = String.format("New complaint submitted: %s (Shipment ID: %d)", event.getSubject(),
                event.getShipmentId());
        service.createNotification(event.getMerchantId(), message, "SMS");

    }

    @Async
    @EventListener
    public void handleComplaintStatusUpdateEvent(ComplaintStatusUpdateEvent event) {
        String message = "Complaint status updated to " + event.getStatus();
        if (event.getNote() != null && !event.getNote().isEmpty()) {
            message += ". Note: " + event.getNote();
        }
        service.createNotification(event.getMerchantId(), message, "SMS");
    }

}
