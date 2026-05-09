package com.gotrack.core_logistic.model.dto;

import com.gotrack.core_logistic.enums.PickupStatus;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickupResponceDTO {
    
    private PickupStatus status;

    @Positive(message = "Courier id must be positive")
    private long curierId;

}
