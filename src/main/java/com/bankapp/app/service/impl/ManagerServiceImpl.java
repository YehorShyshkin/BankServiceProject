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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;
    private final ClientService clientService;

    @Override
    public List<ManagerDTO> findAll() {
        return managerMapper.toDTO(managerRepository.findAll());
    }

    @Override
    public ManagerDTO getManagerDTO(String id) {
        Optional<Manager> managerOptional = managerRepository.findById(UUID.fromString(id));
        Manager manager = managerOptional.orElseThrow(() -> new NoSuchElementException("Manager not found!"));
        return managerMapper.toDTO(manager);
    }

    @Override
    public void save(Manager manager) {
        managerRepository.save(manager);
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
        Client currentClient = clientService.findClientById(clientId); // Получаем клиента по его ID
        Manager currentManager = findManagerById(managerId); // Получаем менеджера по его ID
        if (currentClient != null && currentManager != null) {
            currentClient.setManager(currentManager); // Устанавливаем менеджера для клиента
            clientService.save(currentClient); // Сохраняем клиента в базу данных
            return true; // Возвращаем true, чтобы показать, что операция выполнена успешно
        }
        return false; // Возвращаем false, если клиент или менеджер не найдены
    }
}
