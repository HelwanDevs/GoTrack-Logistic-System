package com.gotrack.support_and_notifications_service.complaint.service;

import java.util.List;

import com.gotrack.support_and_notifications_service.complaint.dto.request.CreateComplaint;
import com.gotrack.support_and_notifications_service.complaint.dto.request.UpdateComplaintStatus;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintCreated;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintResponse;
import com.gotrack.support_and_notifications_service.complaint.dto.response.ComplaintStatusUpdate;
import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;

public interface ComplaintServiceInt {

    ComplaintCreated createComplaint(CreateComplaint request);

    ComplaintStatusUpdate updateComplaintStatus(String complaintId, UpdateComplaintStatus request);

    List<ComplaintResponse> searchComplaints(ComplaintStatus status, String subject, Long shipmentId);

    List<ComplaintResponse> getMyComplaints();

}
