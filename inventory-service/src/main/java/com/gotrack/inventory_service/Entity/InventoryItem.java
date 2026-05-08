package com.gotrack.inventory_service.Entity;

import com.gotrack.inventory_service.Enums.InventoryStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Product is required")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull(message = "Branch ID is required")
    @Min(value = 1, message = "Branch ID must be a positive number")
    @Column(name = "branch_id", nullable = false)
    private Integer branchId;

    @NotBlank(message = "Unique SKU is required")
    @Column(unique = true, nullable = false)
    private String uniqueSku;

    @NotNull(message = "Status is required")  
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryStatus status;


    @NotNull(message = "Pickup request ID is required")
    @Column(name = "pickup_request_id", nullable = false)
    private Long pickupRequestId;
}