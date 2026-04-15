package com.gotrack.inventory_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "products") 
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class Product {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    private String name;

    private Double price;

    private Integer quantity;
}