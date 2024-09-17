package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.exception.ClientNotFoundException;
import com.bankapp.app.exception.ManagerNotFoundException;
import com.bankapp.app.mapper.ClientMapper;
import com.bankapp.app.model.Client;
import com.bankapp.app.model.Manager;
import com.bankapp.app.model.enums.ClientStatus;
import com.bankapp.app.repository.ClientRepository;
import com.bankapp.app.repository.ManagerRepository;
import com.bankapp.app.service.ClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public ClientDTO findClientById(UUID clientId) {
        return clientMapper.toDto(findById(clientId));
    }

    @Override
    @Transactional
    public Client findById(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException
                        (String.format("Client with id %s not found", clientId)));
    }


    @Override
    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client newClient = clientMapper.toEntity(clientDTO);
        Manager manager = managerRepository.findById(clientDTO.getManagerId())
                .orElseThrow(() -> new ManagerNotFoundException("Manager with id %s not found"));
        newClient.setManager(manager);
        Client savedClient = clientRepository.save(newClient);
        return clientMapper.toDto(savedClient);
    }

    @Override
    @Transactional
    public ClientDTO updateClient(UUID clientId, ClientDTO clientDTO) {
        Client client = findById(clientId);
        clientMapper.updateClientFromDto(clientDTO, client);
        Client updatedClient = clientRepository.save(client);
        return clientMapper.toDto(updatedClient);
    }

    @Override
    @Transactional
    public ClientDTO softDeleteClient(UUID clientId) {
        Client client = findById(clientId);
        client.setStatus(ClientStatus.DELETED);
        clientRepository.save(client);
        return clientMapper.toDto(client);
    }
}
