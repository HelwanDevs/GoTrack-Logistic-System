package com.gotrack.user_service.domain.dto;

import com.gotrack.user_service.domain.entity.ProfileStatus;
import com.gotrack.user_service.domain.entity.ProfileType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProfileResponseDTO {

    private Long id;

    private String fullName;
    private String phoneNumber;

    private ProfileType type;

    private Long accountId;
    private Long branchId;

    private ProfileStatus status;
    
    private LocalDateTime createdAt;
}