package com.gotrack.inventory_service.Dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min=2, max=100, message= "Name must be between 2 and 100 characters!")
    private String name;

    @NotBlank(message = "Location is required")
    @Size(min=2, max=100, message= "Location must be between 2 and 100 characters!")
    private String location;

    @NotBlank(message = "Phone is required")
    @Pattern(
            regexp = "^(010|011|012|015)[0-9]{8}$",
            message = "Phone must start with 010, 011, 012, or 015 and be exactly 11 digits"
    )
    private String phone;
}
