package com.gotrack.core_logistic.finance_service.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import com.gotrack.core_logistic.finance_service.enums.ShipmentStatus;

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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class Shipment {

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "pickup_request_id", nullable = false)
    private Pickup pickupRequest;
    
    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
    private List<ShipmentItem> items;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "courier_id")
    private Long courierId;
    

    @Column(name = "flyer_number" , nullable = false ,updatable=false )
    private Long flyerNumber;

    private String note;
    
    @Column(name = "last_update")
    @UpdateTimestamp
    private LocalDateTime lastUpDate;

    @Enumerated
    @Column(nullable = false)
    private ShipmentStatus status;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "shipment_fee", nullable = false)
    private BigDecimal ShipmentFee;


    @Column(name = "delivery_date")
    private LocalDateTime DeliveryDate;

}   