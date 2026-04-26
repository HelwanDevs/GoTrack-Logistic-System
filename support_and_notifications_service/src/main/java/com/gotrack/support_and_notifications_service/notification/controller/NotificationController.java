package com.gotrack.support_and_notifications_service.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.support_and_notifications_service.notification.entity.Notifications;
import com.gotrack.support_and_notifications_service.notification.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notifications>> getMyNotifications() {
        List<Notifications> notifications = service.getNotificationsForProfile();
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Notifications> createAdminNotification(@RequestParam String profileId,
            @RequestParam String message,
            @RequestParam(defaultValue = "IN_APP") String channel) {
        Notifications notification = service.createAdminNotification(profileId, message, channel);
        return ResponseEntity.ok(notification);
    }
}
