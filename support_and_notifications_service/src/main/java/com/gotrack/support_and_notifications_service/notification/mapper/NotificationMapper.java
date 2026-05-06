package com.gotrack.support_and_notifications_service.notification.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;
import com.gotrack.support_and_notifications_service.notification.entity.Notifications;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "profileId", source = "profileId")
    @Mapping(target = "message", source = "message")
    @Mapping(target = "channel", source = "channel")
    @Mapping(target = "isRead", constant = "false")
    Notifications toNotificationMessage(String profileId, String message, NotificationChannel channel);

}
