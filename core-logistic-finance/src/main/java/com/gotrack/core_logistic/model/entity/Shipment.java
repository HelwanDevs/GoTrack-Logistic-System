package com.gotrack.core_logistic.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.gotrack.core_logistic.enums.ShipmentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Shipment {

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "pickup_request_id")
    private Pickup pickupRequest;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private List<ShipmentItem> items;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MERCHANT_id", nullable = false)
    private Long MERCHANTId;

    @Column(name = "courier_id")
    private Long courierId;

    @Column(name = "flyer_number", updatable = false, nullable = false)
    private Long flyerNumber;

    private String note;

    @Column(name = "last_update")
    @UpdateTimestamp
    private LocalDateTime lastUpDate;

    @Enumerated
    private ShipmentStatus status;

    @Column(name = "total_price")
    @Positive(message = "Total price must be greater than zero")
    private BigDecimal totalPrice;

    @Column(name = "shipment_fee", nullable = false)
    @Positive(message = "Shipment fee must be greater than zero")
    private BigDecimal ShipmentFee;

    @Column(name = "delivery_date")
    private LocalDateTime DeliveryDate;

}