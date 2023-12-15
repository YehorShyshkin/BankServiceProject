package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Client;
import com.bankapp.app.entity.Manager;
import com.bankapp.app.mapper.ManagerMapper;
import com.bankapp.app.repository.ManagerRepository;
import com.bankapp.app.service.ClientService;
import com.bankapp.app.service.ManagerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;
    private final ClientService clientService;

    @Override
    public List<ManagerDTO> findAll() {
        return managerMapper.listToDTO(managerRepository.findAll());
    }

    @Override
    public List<ManagerDTO> findManagerDTOByIdList(UUID managerId) {
        return managerRepository.findManagerById(managerId);
    }

    @Override
    public ManagerDTO createManager(Manager manager) {
        return managerMapper.toDTO(managerRepository.save(manager));
    }

    @Override
    public ManagerDTO findManagerDTOById(UUID managerId) {
        return managerMapper.toDTO(findManagerById(managerId));
    }

    @Override
    public Manager findManagerById(UUID managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found!"));
    }

    @Override
    public Manager updateManager(UUID id, ManagerDTO managerDTO) {
        Manager currentManager = findManagerById(id);
        Manager updateManager = managerMapper.updateManagerFromDTO(managerDTO, currentManager);
        return managerRepository.save(updateManager);
    }

    @Override
    public boolean deleteManager(UUID id) {
        if (managerRepository.existsById(id)) {
            managerRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mergeManagerAndClient(UUID clientId, UUID managerId) {
        Client currentClient = clientService.findClientById(clientId);
        Manager currentManager = findManagerById(managerId);
        if (currentClient != null && currentManager != null) {
            currentClient.setManager(currentManager);
            clientService.createClientDTO(currentClient);
            return true;
        }
        return false;
    }
}
