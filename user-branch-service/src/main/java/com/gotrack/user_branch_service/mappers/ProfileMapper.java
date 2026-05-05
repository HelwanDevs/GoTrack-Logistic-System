package com.gotrack.user_branch_service.mappers;

public interface ProfileMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
}