package com.bankapp.app.mapper;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ManagerMapper {

    ManagerDTO toDTO(Manager manager);

    List<ManagerDTO> listToDTO(List<Manager> managerList);

    Manager updateManagerFromDTO(ManagerDTO managerDTO, @MappingTarget Manager manager);

}
