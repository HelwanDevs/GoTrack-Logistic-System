package com.gotrack.support_and_notifications_service.complaint.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gotrack.support_and_notifications_service.complaint.entity.Complaint;
import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;

public interface ComplaintRepo extends MongoRepository<Complaint, String> {

    Optional<Complaint> findByIdAndMerchantId(String id, Long merchantId);

    List<Complaint> findByMerchantIdOrderByCreatedAtDesc(Long merchantId);

    List<Complaint> findByMerchantIdAndStatusInOrderByCreatedAtDesc(Long merchantId, List<ComplaintStatus> statuses);

    List<Complaint> findByStatusInOrderByCreatedAtDesc(List<ComplaintStatus> statuses);

    List<Complaint> findBySubjectContainingIgnoreCaseOrderByCreatedAtDesc(String subject);

    List<Complaint> findByShipmentIdOrderByCreatedAtDesc(Long shipmentId);
}