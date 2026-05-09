package com.gotrack.core_logistic.model.dto.DTOFilters;

import lombok.Data;

@Data
public class TransactionFilter {
    
    private Long fromProfileId;
    private Long toProfileId;
    private String type;
}
