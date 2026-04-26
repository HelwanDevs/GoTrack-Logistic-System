package com.gotrack.support_and_notifications_service.notification.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Notification")
public class Notifications {

    @Id
    private String id;

    private Long profileId;

    private String message;

    private String channel;

    @CreatedDate
    private Instant sent_at;

    private boolean isRead;

}
