package com.gotrack.branch_service.mappers.impl;

import com.gotrack.branch_service.domain.dto.BranchDTO;
import com.gotrack.branch_service.domain.entity.BranchEntity;
import com.gotrack.branch_service.mappers.Mapper;
import org.apache.catalina.Manager;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BranchMapper implements Mapper<BranchEntity, BranchDTO> {

    private ModelMapper modelMapper;

    public BranchMapper(ModelMapper modelMapper) {
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
