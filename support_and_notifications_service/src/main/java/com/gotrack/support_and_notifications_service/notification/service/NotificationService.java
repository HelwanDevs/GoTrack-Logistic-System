package com.gotrack.support_and_notifications_service.notification.service;

import com.gotrack.support_and_notifications_service.user.CheckUser;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.support_and_notifications_service.error.ResourceNotFoundException;
import com.gotrack.support_and_notifications_service.eventWrapper.WEventPublisher;
import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;
import com.gotrack.support_and_notifications_service.notification.entity.Notifications;
import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;
import com.gotrack.support_and_notifications_service.notification.mapper.NotificationMapper;
import com.gotrack.support_and_notifications_service.notification.repo.NotificationRepo;
import com.gotrack.support_and_notifications_service.user.CurrentUserService;
import com.gotrack.support_and_notifications_service.error.*;

@Service
public class NotificationService {

    @Autowired
    private CheckUser checkUser;
    @Autowired
    private NotificationRepo repo;
    @Autowired
    private NotificationMapper mapper;
    @Autowired
    private WEventPublisher eventPublisher;
    @Autowired
    private CurrentUserService user;

    public Notifications createNotification(String profileId, String message, NotificationChannel channel,
            String email) {
        Notifications noti = mapper.toNotificationMessage(profileId, message, channel);
        noti.setSentAt(Instant.now());
        Notifications saved = repo.save(noti);
        eventPublisher.publish(new NotificationCreated(
                saved.getId(),
                saved.getProfileId(),
                saved.getMessage(),
                saved.getChannel(),
                email));
        return saved;
    }

    public List<Notifications> getNotificationsForProfile() {
        String profileId = user.getCurrentUserId();
        return repo.findByProfileIdOrderBySentAtDesc(profileId);
    }

    public Notifications createAdminNotification(String profileId, String message, NotificationChannel channel) {

        checkUser.checkUserExists(profileId);
        Notifications notification = Notifications.builder()
                .profileId(profileId)
                .message(message)
                .channel(channel)
                .isRead(false)
                .sentAt(Instant.now())
                .build();

        return repo.save(notification);
    }

    public List<Notifications> getUnreadNotificationsForProfile() {
        String profileId = user.getCurrentUserId();
        return repo.findByProfileIdAndIsReadFalseOrderBySentAtDesc(profileId);
    }

    public void markAsRead(String notificationId, String profileId) {
        checkUser.checkUserExists(profileId);
        Notifications noti = repo.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification " + notificationId + " not found"));
        if (!noti.getProfileId().equals(profileId)) {
            throw new AccessDeniedException("Not allowed");
        }

        noti.setRead(true);
        repo.save(noti);
    }

}
