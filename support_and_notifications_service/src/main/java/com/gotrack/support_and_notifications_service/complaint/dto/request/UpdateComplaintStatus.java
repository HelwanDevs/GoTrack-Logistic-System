package com.gotrack.support_and_notifications_service.complaint.dto.request;

import java.util.List;

import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;
import com.gotrack.support_and_notifications_service.notification.entity.NotificationChannel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UpdateComplaintStatus {

    @NotNull(message = "Invalid status update")
    private ComplaintStatus status;

    @NotBlank(message = "Invalid status update")
    private String note;

    private List<NotificationChannel> channel;
}
