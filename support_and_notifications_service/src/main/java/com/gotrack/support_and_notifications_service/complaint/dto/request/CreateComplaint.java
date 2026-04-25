package com.gotrack.support_and_notifications_service.complaint.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class CreateComplaint {

    private Long shipmentId;

    @NotBlank(message = "Subject or content missing")
    @Size(max = 200, message = "Subject is too long")
    private String subject;

    @NotBlank(message = "Subject or content missing")
    @Size(max = 4000, message = "Content is too long")
    private String content;
}
