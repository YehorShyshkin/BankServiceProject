package com.bankapp.app.service;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.model.Client;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ClientService {
    ClientDTO findClientById(UUID clientId);

    Client findById(UUID clientId);

    ClientDTO createClient(ClientDTO clientDTO);

    ClientDTO updateClient(UUID clientId, @Valid ClientDTO clientDTO);

    ClientDTO softDeleteClient(UUID clientId);
}
