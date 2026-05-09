package com.gotrack.support_and_notifications_service.notification.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;
import com.gotrack.support_and_notifications_service.notification.entity.Notifications;

public interface NotificationRepo extends MongoRepository<Notifications, String> {

    List<Notifications> findByProfileIdOrderBySentAtDesc(String profileId);

    List<Notifications> findByProfileIdAndIsReadOrderBySentAtDesc(String profileId, boolean isRead);

    List<Notifications> findByProfileIdAndIsReadFalseAndChannelOrderBySentAtDesc(String profileId,
            NotificationChannel channel);

    List<Notifications> findByProfileIdAndChannelOrderBySentAtDesc(String profileId, NotificationChannel channel);

    List<Notifications> findByProfileIdAndIsReadAndChannelOrderBySentAtDesc(String profileId, boolean isRead,
            NotificationChannel channel);
}
