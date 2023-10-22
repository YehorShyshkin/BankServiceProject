package com.bankapp.app.mapper;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.entity.Client;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR, uses = {AccountMapper.class})
public interface ClientMapper {

    @Mapping(source = "clientStatus", target = "clientStatus")
    @Mapping(source = "clientFirstName", target = "clientFirstName")
    @Mapping(source = "clientLastName", target = "clientLastName")
    @Mapping(source = "manager.managerLastName", target = "managerLastName")
    @Mapping(source = "manager.managerStatus", target = "managerStatus")

    ClientDTO toDTO(Client client);
    List<ClientDTO> toDTO(List<Client> clientList);
}
