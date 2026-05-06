package com.gotrack.core_logistic.mapper;

import org.mapstruct.Mapper;

import com.gotrack.core_logistic.model.dto.FinancialSummaryDTO;
import com.gotrack.core_logistic.model.entity.FinancialSummary;

@Mapper(componentModel = "spring")
public interface FinancialSummaryMapper {
       
      FinancialSummary toEntity(FinancialSummaryDTO DTO);

      FinancialSummaryDTO toDTO(FinancialSummary entity);

}
