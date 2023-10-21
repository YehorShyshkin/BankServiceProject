package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ClientDTO;
import com.bankapp.app.mapper.ClientMapper;
import com.bankapp.app.repository.ClientRepository;
import com.bankapp.app.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        return clientMapper.toDTO(clientRepository.findById(UUID.fromString(id)).orElseThrow());
    }
}
