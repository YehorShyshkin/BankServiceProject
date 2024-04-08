package com.bankapp.app.service;

import com.bankapp.app.controller.dto.ClientDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.entity.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<ClientDTO> findAll();
    ClientDTO getClientDTO(String id);

    Client findClientById(UUID id);

    List<Account> getAccountsForClient(UUID clientId);

    ClientDTO createClientDTO(Client client);

}
