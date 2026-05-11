package com.gotrack.core_logistic.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.gotrack.core_logistic.enums.PickupStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pickup_requests")
@Getter
@Setter
public class Pickup {

    @OneToMany(mappedBy = "pickupRequest", cascade = CascadeType.ALL)
    private List<Shipment> shipments;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MERCHANT_id", nullable = false)
    @NotNull
    private Long MERCHANTId;

    @Column(name = "courier_id")
    private Long courierId;

    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;

    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    private LocalDateTime pickupTime;

    @Enumerated
    private PickupStatus status;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_contact", nullable = false)
    private String receiverContact;

    @Column(name = "receiver_address", nullable = false)
    private String receiverAddress;

    @Column(name = "Shipment_cost", nullable = false)
    private BigDecimal cost;

}