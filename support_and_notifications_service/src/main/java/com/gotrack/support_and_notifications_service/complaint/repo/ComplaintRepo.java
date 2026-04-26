package com.gotrack.support_and_notifications_service.complaint.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gotrack.support_and_notifications_service.complaint.entity.Complaint;
import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;

public interface ComplaintRepo extends MongoRepository<Complaint, String> {

    Optional<Complaint> findByIdAndProfileId(String id, String profileId);

    List<Complaint> findByProfileIdOrderByCreatedAtDesc(String profileId);

    List<Complaint> findByprofileIdAndStatusInOrderByCreatedAtDesc(String profileId, List<ComplaintStatus> statuses);

    List<Complaint> findByStatusInOrderByCreatedAtDesc(List<ComplaintStatus> statuses);

    List<Complaint> findBySubjectContainingIgnoreCaseOrderByCreatedAtDesc(String subject);

    List<Complaint> findByShipmentIdOrderByCreatedAtDesc(String shipmentId);
}