package com.gotrack.support_and_notifications_service.notification.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gotrack.support_and_notifications_service.eventWrapper.WEventPublisher;
import com.gotrack.support_and_notifications_service.notification.entity.Notifications;
import com.gotrack.support_and_notifications_service.notification.event.NotificationCreated;
import com.gotrack.support_and_notifications_service.notification.mapper.NotificationMapper;
import com.gotrack.support_and_notifications_service.notification.repo.NotificationRepo;
import com.gotrack.support_and_notifications_service.user.CurrentUserService;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo repo;
    @Autowired
    private NotificationMapper mapper;
    @Autowired
    private WEventPublisher eventPublisher;
    @Autowired
    private CurrentUserService user;

    public Notifications createNotification(String profileId, String message, String channel) {
        Notifications noti = mapper.toNotificationMessage(profileId, message);
        noti.setChannel(channel);
        noti.setSentAt(Instant.now());
        Notifications saved = repo.save(noti);
        eventPublisher.publish(new NotificationCreated(
                saved.getId(),
                saved.getProfileId(),
                saved.getMessage(),
                saved.getChannel()));
        return saved;
    }

    public List<Notifications> getNotificationsForProfile() {
        String profileId = user.getCurrentUserId();
        return repo.findByProfileIdOrderBySentAtDesc(profileId);
    }

    public Notifications createAdminNotification(String profileId, String message, String channel) {

        Notifications notification = Notifications.builder()
                .profileId(profileId)
                .message(message)
                .channel(channel)
                .isRead(false)
                .sentAt(Instant.now())
                .build();

        return repo.save(notification);
    }

}
