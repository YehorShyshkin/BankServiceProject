package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.entity.Client;
import com.bankapp.app.mapper.ClientMapper;
import com.bankapp.app.repository.ClientRepository;
import com.bankapp.app.service.ClientService;
import jakarta.persistence.EntityNotFoundException;
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
        return clientMapper.listToDTO(clientRepository.findAll());
    }

    @Override
    public ClientDTO getClientDTO(String id) {
        Optional<Client> clientOptional = clientRepository.findById(UUID.fromString(id));
        Client client = clientOptional.orElseThrow(() -> new NoSuchElementException("Client not found!"));
        return clientMapper.toDTO(client);
    }

    @Override
    public Client findClientById(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found!"));
    }

    @Override
    public ClientDTO createClientDTO(Client client) {
        return clientMapper.toDTO(clientRepository.save(client));
    }

    @Override
    public List<Account> getAccountsForClient(UUID clientId) {
        Client client = findClientById(clientId);
        if (client != null) {
            return client.getAccounts();
        }
        return Collections.emptyList();
    }


}
