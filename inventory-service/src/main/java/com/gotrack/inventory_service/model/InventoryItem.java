package com.gotrack.inventory_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data  
@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long branchId;

    @Column(nullable = false)
    private Long pickupRequestId;

    @Column(unique = true, nullable = false)
    private String uniqueSku;
}