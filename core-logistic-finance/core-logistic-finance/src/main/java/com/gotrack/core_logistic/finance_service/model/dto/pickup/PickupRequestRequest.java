package com.gotrack.core_logistic.finance_service.model.dto.pickup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PickupRequestRequest {

    private Long customerId;
    private Long courierId;

    private String pickupAddress;
    private String notes;

    private String receiverName;
    private String receiverContact;
    private String receiverAddress;
    
}