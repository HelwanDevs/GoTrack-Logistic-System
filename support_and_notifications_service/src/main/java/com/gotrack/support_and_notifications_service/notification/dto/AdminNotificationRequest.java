package com.gotrack.support_and_notifications_service.notification.dto;

import java.util.List;

import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminNotificationRequest {

    @NotBlank(message = "Profile ID is required")
    private String profileId;

    @NotBlank(message = "Message is required")
    private String message;

    @NotNull(message = "Notification channel is required")
    private List<NotificationChannel> channel;

    @NotBlank(message = "Email is required")
    private String email;

}
