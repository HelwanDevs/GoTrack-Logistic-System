package com.gotrack.support_and_notifications_service.complaint.dto.response;

import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;
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
public class ComplaintResponse {
    private String id;
    private String subject;
    private ComplaintStatus status;
}
