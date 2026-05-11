package com.gotrack.inventory_service.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
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
@Table(name = "products")

public class Product {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Merchant ID is required")
    @Column(name = "merchant_id" ,nullable = false)
     private Long merchantId;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<InventoryItem> inventoryItems;

    @NotBlank(message = "Base SKU is required")
    @Column(unique = true, nullable = false)
    private String baseSku;
}
