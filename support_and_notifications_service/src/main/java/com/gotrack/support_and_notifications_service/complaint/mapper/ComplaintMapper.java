package com.gotrack.support_and_notifications_service.complaint.mapper;

import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintCreated;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintResponse;
import com.gotrack.support_and_notifications_service.complaint.entity.Complaint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComplaintMapper {

    @Mapping(target = "complaintId", source = "id")
    @Mapping(target = "message", constant = "Complaint created successfully")
    ComplaintCreated toCreatedResponse(Complaint complaint);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "subject", source = "subject")
    @Mapping(target = "status", source = "status")
    ComplaintResponse toResponse(Complaint complaint);

    List<ComplaintResponse> toResponseList(List<Complaint> complaints);

}
