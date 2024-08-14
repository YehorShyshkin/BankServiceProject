package com.bankapp.app.mapper;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.model.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy
        = NullValuePropertyMappingStrategy.IGNORE)
public interface ManagerMapper {
    void updateManagerDto(ManagerDTO managerDTO, @MappingTarget Manager manager);
    Manager toEntity(ManagerDTO managerDTO);
    ManagerDTO toDto(Manager manager);
}

