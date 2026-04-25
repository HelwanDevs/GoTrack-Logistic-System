package com.gotrack.support_and_notifications_service.complaint.mapper;

import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintCreated;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintResponse;
import com.gotrack.support_and_notifications_service.complaint.entity.Complaint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ComplaintMapper {

    public ComplaintCreated toCreatedResponse(Complaint complaint) {
        return ComplaintCreated.builder()
                .complaintId(complaint.getId())
                .message("Complaint created successfully")
                .build();
    }

    public ComplaintResponse toResponse(Complaint complaint) {
        return ComplaintResponse.builder()
                .id(complaint.getId())
                .subject(complaint.getSubject())
                .status(complaint.getStatus())
                .build();
    }

    public List<ComplaintResponse> toResponseList(List<Complaint> complaints) {
        return complaints.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

}
