package com.gotrack.inventory_service.Entity;

import java.util.List;

import jakarta.persistence.*;
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
   
    private String name;
    @Column(name = "merchant_id" ,nullable = false)
     private Long merchantId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<inventoryItem> inventoryItems;

    private String baseSku;
}
