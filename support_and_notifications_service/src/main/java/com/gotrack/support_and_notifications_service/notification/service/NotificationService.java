package com.gotrack.support_and_notifications_service.notification.service;

import java.time.Instant;
import java.util.List;

import javax.management.Notification;

import org.apache.tomcat.util.modeler.NotificationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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

    public Notifications createNotification(long profileId, String message, String channel) {
        Notifications noti = mapper.toNotificationMessage(profileId, message);
        noti.setChannel(channel);
        noti.setSent_at(Instant.now());
        Notifications saved = repo.save(noti);
        eventPublisher.publish(new NotificationCreated(
                saved.getId(),
                saved.getProfileId(),
                saved.getMessage(),
                saved.getChannel()));
        return saved;
    }

    public List<Notifications> getNotificationsForProfile() {
        long profileId = user.getCurrentUserId();
        return repo.findByProfileIdOrderBySentAtDesc(profileId);
    }

}
