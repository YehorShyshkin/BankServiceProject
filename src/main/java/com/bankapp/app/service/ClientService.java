package com.bankapp.app.service;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.model.Account;
import com.bankapp.app.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<ClientDTO> findAll();
    ClientDTO getClientDTO(String id);

    Client findClientById(UUID id);

    List<Account> getAccountsForClient(UUID clientId);

    ClientDTO createClientDTO(Client client);

}
