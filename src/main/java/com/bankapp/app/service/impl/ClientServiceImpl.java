package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.entity.Client;
import com.bankapp.app.mapper.ClientMapper;
import com.bankapp.app.repository.ClientRepository;
import com.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientDTO> findAll() {
        return clientMapper.toDTO(clientRepository.findAll());
    }

    @Override
    public ClientDTO getClientDTO(String id) {
        Optional<Client> clientOptional = clientRepository.findById(UUID.fromString(id));
        Client client = clientOptional.orElseThrow(() -> new NoSuchElementException("Client not found!"));
        return clientMapper.toDTO(client);
    }

    @Override
    public Client getClient(UUID id) {
        return clientRepository.findById(id).orElseThrow(null);
    }

    @Override
    public void save(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<Account> getAccountsForClient(UUID clientId) {
        Client client = getClient(clientId);
        if (client != null) {
            return client.getAccounts();
        }
        return Collections.emptyList();
    }


}
