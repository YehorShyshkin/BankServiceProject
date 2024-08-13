package com.bankapp.app.mapper;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy
        = NullValuePropertyMappingStrategy.IGNORE)
public interface ManagerMapper {

    ManagerDTO toDto (Manager manager);

    Manager toEntity(ManagerDTO managerDTO);

}
