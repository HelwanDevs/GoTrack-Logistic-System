package com.gotrack.core_logistic.finance_service.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.gotrack.core_logistic.finance_service.enums.PickupStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "pickup_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickupRequest {
    
    @OneToMany(mappedBy = "pickupRequest", cascade = CascadeType.ALL)
    private List<Shipment> shipments;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    @Column(name = "courier_id")
    private Long courierId;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;
    private String notes;

    @UpdateTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private PickupStatus status;
    
    @Column(name = "receiver_name", nullable = false)
    private String receiverName;
    @Column(name = "receiver_contact", nullable = false)
    private String receiverContact;
    @Column(name = "receiver_address", nullable = false)
    private String receiverAddress;

}