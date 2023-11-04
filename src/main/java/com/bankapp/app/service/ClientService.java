package com.bankapp.app.service;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.entity.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<ClientDTO> findAll();
    ClientDTO getClientDTO(String id);

    Client getClient(UUID id);

    void save(Client client);

}
