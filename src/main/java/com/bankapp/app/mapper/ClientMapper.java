package com.bankapp.app.mapper;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClientMapper {
    @Mapping(source = "managerId", target = "manager.id")
    Client toEntity(ClientDTO clientDTO);

    @Mapping(source = "manager.id", target = "managerId")
    ClientDTO toDto(Client client);

    void updateClientFromDto(ClientDTO clientDTO, @MappingTarget Client client);
}