package com.gotrack.support_and_notifications_service.notification.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.support_and_notifications_service.notification.dto.AdminNotificationRequest;
import com.gotrack.support_and_notifications_service.notification.entity.Notifications;
import com.gotrack.support_and_notifications_service.notification.service.NotificationService;
import com.gotrack.support_and_notifications_service.user.CurrentUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService service;
    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Notifications>> getMyNotifications(@RequestParam(required = false) Boolean readStatus) {
        // i'll make it if null then get all, 1 for read, 0 for unread
        List<Notifications> notifications = service.getNotificationsForProfile(readStatus);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Notifications>> createAdminNotification(
            @Valid @RequestBody AdminNotificationRequest request) {
        List<Notifications> notification = service.createAdminNotification(request.getProfileId(), request.getMessage(),
                request.getChannel(), request.getEmail());
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable("id") String notificationId) {
        String profileId = currentUserService.getCurrentUserId();

        service.markAsRead(notificationId, profileId);

        return ResponseEntity.ok(
                Map.of("message", "Notification marked as read"));
    }

    @PutMapping("/{id}/unread")
    public ResponseEntity<?> markAsUnread(@PathVariable("id") String notificationId) {
        String profileId = currentUserService.getCurrentUserId();

        service.markAsUnread(notificationId, profileId);

        return ResponseEntity.ok(
                Map.of("message", "Notification marked as unread"));
    }
}
