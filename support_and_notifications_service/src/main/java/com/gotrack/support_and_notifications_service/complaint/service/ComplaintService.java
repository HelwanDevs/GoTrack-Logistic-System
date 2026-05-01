package com.gotrack.support_and_notifications_service.complaint.service;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotrack.support_and_notifications_service.complaint.entity.Complaint;
import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;
import com.gotrack.support_and_notifications_service.complaint.dto.request.CreateComplaint;
import com.gotrack.support_and_notifications_service.complaint.dto.request.UpdateComplaintStatus;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintResponse;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintStatusUpdate;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintCreated;
import com.gotrack.support_and_notifications_service.complaint.mapper.ComplaintMapper;
import com.gotrack.support_and_notifications_service.complaint.repo.ComplaintRepo;
import com.gotrack.support_and_notifications_service.user.CurrentUserService;
import com.gotrack.support_and_notifications_service.error.*;
import com.gotrack.support_and_notifications_service.eventWrapper.WEventPublisher;
import com.gotrack.support_and_notifications_service.notification.event.ComplaintStatusUpdateEvent;
import com.gotrack.support_and_notifications_service.notification.event.ComplaintSubmit;
import com.gotrack.support_and_notifications_service.shipment.CheckShipment;

@Service
public class ComplaintService implements ComplaintServiceInt {

    @Autowired
    private ComplaintRepo repo;
    @Autowired
    private ComplaintMapper mapper;
    @Autowired
    private CurrentUserService user;
    @Autowired
    private CheckShipment shipment;
    @Autowired
    private WEventPublisher eventPublisher;
    @Autowired
    private CheckStatusTransition checkTransition;

    @Transactional
    @Override
    public ComplaintCreated createComplaint(CreateComplaint request) {
        String profileId = user.getCurrentUserId();
        String email = user.getUserEmail();
        if (request.getShipmentId() != null) {
            shipment.checkShipmentExists(request.getShipmentId());
        }

        Complaint complaint = Complaint.builder()
                .profileId(profileId)
                .email(email)
                .shipmentId(request.getShipmentId())
                .subject(request.getSubject())
                .content(request.getContent())
                .createdAt(Instant.now())
                .status(ComplaintStatus.PENDING)
                .build();

        Complaint saved = repo.save(complaint);
        eventPublisher.publish(
                new ComplaintSubmit(saved.getId(), profileId, email, saved.getSubject(), saved.getShipmentId(),
                        request.getChannel()));

        return mapper.toCreatedResponse(saved);
    }

    @Transactional
    @Override
    public ComplaintStatusUpdate updateComplaintStatus(String complaintId, UpdateComplaintStatus request) {
        Complaint complaint = repo.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        if (request.getStatus() == null || request.getNote() == null || request.getNote().isBlank()) {
            throw new BadRequestException("Invalid status update");
        }
        String email = complaint.getEmail();

        checkTransition.checkTransition(complaint.getStatus(), request.getStatus());

        complaint.setStatus(request.getStatus());
        complaint.setNote(request.getNote());
        Complaint updated = repo.save(complaint);

        eventPublisher.publish(new ComplaintStatusUpdateEvent(
                updated.getId(),
                updated.getProfileId(), email, updated.getStatus(), updated.getNote(), request.getChannel()));

        return ComplaintStatusUpdate.builder()
                .message("Complaint status updated")
                .build();
    }

    @Override
    public List<ComplaintResponse> searchComplaints(ComplaintStatus status, String subject, Long shipmentId) {
        List<Complaint> complaints = repo.findAll();

        if (status != null) {
            complaints = complaints.stream()
                    .filter(c -> c.getStatus() == status)
                    .toList();
        }

        if (subject != null && !subject.isBlank()) {
            complaints = complaints.stream()
                    .filter(c -> c.getSubject().toLowerCase().contains(subject.toLowerCase()))
                    .toList();
        }

        if (shipmentId != null) {
            complaints = complaints.stream()
                    .filter(c -> shipmentId.equals(c.getShipmentId()))
                    .toList();
        }

        return mapper.toResponseList(complaints);
    }

    @Override
    public List<ComplaintResponse> getMyComplaints() {
        String profileId = user.getCurrentUserId();
        List<Complaint> complaints = repo.findByProfileIdOrderByCreatedAtDesc(profileId);
        return mapper.toResponseList(complaints);
    }

}
