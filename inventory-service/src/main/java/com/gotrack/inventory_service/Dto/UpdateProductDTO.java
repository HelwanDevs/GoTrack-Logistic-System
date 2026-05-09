package com.gotrack.inventory_service.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UpdateProductDTO {
    
    @Positive(message = "Product ID must be a positive number")
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Merchant ID is required")
    @Positive(message = "Merchant ID must be a positive number")
    private Long merchantId;
  
}
