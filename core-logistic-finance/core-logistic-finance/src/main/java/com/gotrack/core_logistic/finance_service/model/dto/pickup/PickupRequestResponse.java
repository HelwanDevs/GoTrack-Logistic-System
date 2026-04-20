package com.gotrack.core_logistic.finance_service.model.dto.pickup;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PickupRequestResponse {

    private Long id;

    private Long customerId;
    private Long courierId;

    private String pickupAddress;
    private String notes;

    private String receiverName;
    private String receiverContact;
    private String receiverAddress;

    private String status;

    private LocalDateTime lastUpdate;
}