package com.gotrack.support_and_notifications_service.complaint.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.gotrack.support_and_notifications_service.complaint.service.ComplaintService;
import com.gotrack.support_and_notifications_service.complaint.dto.request.CreateComplaint;
import com.gotrack.support_and_notifications_service.complaint.dto.request.UpdateComplaintStatus;
import jakarta.validation.Valid;

import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintCreated;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintResponse;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintStatusUpdate;
import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService service;

    @PostMapping
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<ComplaintCreated> createComplaint(@Valid @RequestBody CreateComplaint request) {
        ComplaintCreated response = service.createComplaint(request);

        return ResponseEntity.status(201).body(response);

    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ComplaintStatusUpdate> updateComplaintStatus(@PathVariable("id") String complainId,
            @Valid @RequestBody UpdateComplaintStatus request) {
        ComplaintStatusUpdate response = service.updateComplaintStatus(complainId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<ComplaintResponse>> searchComplaints(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) ComplaintStatus status,
            @RequestParam(required = false) Long shipmentId) {
        List<ComplaintResponse> response = service.searchComplaints(status, subject, shipmentId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('MERCHANT')")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints() {
        List<ComplaintResponse> response = service.getMyComplaints();

        return ResponseEntity.ok(response);
    }

}
