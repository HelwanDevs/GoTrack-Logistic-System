package com.gotrack.support_and_notifications_service.complaint.service;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.gotrack.support_and_notifications_service.complaint.entity.ComplaintStatus;
import com.gotrack.support_and_notifications_service.error.BadRequestException;
import com.gotrack.support_and_notifications_service.error.InvalidStatusTransitionException;

@Component
public class CheckStatusTransition {
    private static final Map<ComplaintStatus, Set<ComplaintStatus>> transitions = Map.of(
            ComplaintStatus.PENDING, Set.of(ComplaintStatus.IN_PROGRESS),
            ComplaintStatus.IN_PROGRESS, Set.of(ComplaintStatus.RESOLVED),
            ComplaintStatus.RESOLVED, Set.of(ComplaintStatus.COMPENSATED),
            ComplaintStatus.COMPENSATED, Set.of());

    public void checkTransition(ComplaintStatus current, ComplaintStatus next) {

        if (current == next) {
            throw new BadRequestException("Status is already " + current);
        }

        Set<ComplaintStatus> allowed = transitions.getOrDefault(current, Set.of());

        if (!allowed.contains(next)) {
            throw new InvalidStatusTransitionException(
                    "Cannot transition from " + current + " to " + next);
        }
    }
}
