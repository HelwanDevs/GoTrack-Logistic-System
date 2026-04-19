package com.gotrack.user_service.mappers.imp;

import com.gotrack.user_service.domain.dto.ProfileRequestDTO;
import com.gotrack.user_service.domain.dto.ProfileResponseDTO;
import com.gotrack.user_service.domain.dto.ProfileUpdateDTO;
import com.gotrack.user_service.domain.entity.ProfileEntity;
import com.gotrack.user_service.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper implements Mapper<ProfileEntity, ProfileResponseDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProfileResponseDTO toDto(ProfileEntity entity) {
        return modelMapper.map(entity, ProfileResponseDTO.class);
    }

    @Override
    public ProfileEntity toEntity(ProfileResponseDTO dto) {
        return modelMapper.map(dto, ProfileEntity.class);
    }

    public ProfileEntity toEntity(ProfileRequestDTO dto) {
        return modelMapper.map(dto, ProfileEntity.class);
    }

    public void updateEntity(ProfileUpdateDTO dto, ProfileEntity entity) {
        modelMapper.map(dto, entity);
    }
}