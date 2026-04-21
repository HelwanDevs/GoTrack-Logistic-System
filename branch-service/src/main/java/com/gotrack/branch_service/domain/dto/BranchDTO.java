package com.gotrack.branch_service.domain.dto;

import jakarta.persistence.Column;
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
    @Size(min=5, max=100, message= "Name must be between 4 and 100 character!")
    private String name;

    @NotBlank(message = "Location is required")
    @Size(min=5, max=100, message= "Location must be between 4 and 100 character!")
    private String location;

    @Pattern(
            regexp = "^(010|011|012|015)[0-9]{8}$",
            message = "Phone must start with 010, 011, 012, or 015 and be exactly 11 digits"
    )
    private String phone;
}
