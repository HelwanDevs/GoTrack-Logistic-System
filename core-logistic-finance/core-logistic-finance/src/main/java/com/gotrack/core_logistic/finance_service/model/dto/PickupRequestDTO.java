package com.gotrack.core_logistic.finance_service.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gotrack.core_logistic.finance_service.enums.PickupStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PickupRequestDTO {
    
    private Long id;
    
    
    private Long customerId;
    private Long courierId;
    
    private String pickupAddress;
    private String notes;

    @Future(message = "Invalid date formatting")
    private LocalDateTime pickupTime;

    @NotBlank(message = "Receiver name is required") 
    @Pattern(regexp="^[a-zA-Z]+$", message="Receiver name must contain only letters")
    private String receiverName;

    @NotNull(message = "Receiver contact is required")
    @Pattern(regexp="^[0-9]+$", message="Receiver contact must contain only numbers")
    private String receiverContact;

    @NotBlank(message = "Receiver address is required")
    private String receiverAddress;

    private PickupStatus Status;
    
    private BigDecimal cost;

    private LocalDateTime lastUpdate;
    
}