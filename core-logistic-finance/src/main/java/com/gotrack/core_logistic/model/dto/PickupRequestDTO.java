package com.gotrack.core_logistic.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gotrack.core_logistic.enums.PickupStatus;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickupRequestDTO {
    
    
    private Long id;
    
    
    @Positive(message = "Customer ID must be positive")
    private Long customerId;
    
    private Long courierId;
    
    @NotBlank(message = "Pickup address is required")
    private String pickupAddress;


    private String notes;

    @Future(message = "Invalid date formatting")
    private LocalDateTime pickupTime;

    @NotBlank(message = "Receiver name is required") 
    @Pattern(regexp="^[a-zA-Z]+( [a-zA-Z]+)*$", 
         message="Receiver name must contain only letters and single spaces between words")
    private String receiverName;
    
    @NotBlank(message = "Receiver contact is required")
    @Pattern(regexp="^[0-9]+$", message="Receiver contact must contain only numbers")
    private String receiverContact;

    @NotBlank(message = "Receiver address is required")
    private String receiverAddress;

    private PickupStatus Status;
    
    @NotNull(message = "Cost is required")
    private BigDecimal cost;

    private LocalDateTime lastUpdate;
    private LocalDateTime createdAt;
    

}