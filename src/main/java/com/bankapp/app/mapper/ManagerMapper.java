package com.bankapp.app.mapper;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ManagerMapper {

    @Mapping(source = "managerLastName", target = "managerLastName")
    @Mapping(source = "managerStatus", target = "managerStatus")

    ManagerDTO toDTO(Manager manager);
    List<ManagerDTO> toDTO(List<Manager> managerList);

}
