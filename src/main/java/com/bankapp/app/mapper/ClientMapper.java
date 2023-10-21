package com.bankapp.app.mapper;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "clientStatus", target = "clientStatus")
    @Mapping(source = "clientFirstName", target = "clientFirstName")
    @Mapping(source = "clientLastName", target = "clientLastName")
    @Mapping(source = "manager.managerLastName", target = "managerLastName")
    @Mapping(source = "manager.managerStatus", target = "managerStatus")

    ClientDTO toDTO(Client client);
    List<ClientDTO> toDTO(List<Client> clientList);
}
