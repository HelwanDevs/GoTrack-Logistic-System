package com.gotrack.support_and_notifications_service.notification.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.gotrack.support_and_notifications_service.notification.entity.Notifications;

public interface NotificationRepo extends MongoRepository<Notifications, String> {

    List<Notifications> findByProfileIdOrderBySentAtDesc(Long profileId);

    List<Notifications> findByProfileIdAndIsReadFalseOrderBySentAtDesc(Long profileId);

}
