package com.gotrack.branch_service.mappers.impl;

import com.gotrack.branch_service.domain.dto.BranchDTO;
import com.gotrack.branch_service.domain.entity.BranchEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BranchMapperImp implements com.gotrack.branch_service.mappers.BranchMapper<BranchEntity, BranchDTO> {

    private ModelMapper modelMapper;

    public BranchMapperImp(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BranchDTO mapTo(BranchEntity branchEntity) {
       return  modelMapper.map(branchEntity , BranchDTO.class);
    }

    @Override
    public BranchEntity mapFrom(BranchDTO branchDTO) {
        return  modelMapper.map(branchDTO , BranchEntity.class);
    }
}
