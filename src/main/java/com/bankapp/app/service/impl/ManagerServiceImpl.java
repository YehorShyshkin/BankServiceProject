package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.model.Manager;
import com.bankapp.app.exception.ManagerNotFoundException;
import com.bankapp.app.mapper.ManagerMapper;
import com.bankapp.app.model.enums.ManagerStatus;
import com.bankapp.app.repository.ManagerRepository;
import com.bankapp.app.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;
    private final ManagerMapper managerMapper;

    @Override
    public ManagerDTO createManager(ManagerDTO managerDTO) {
        if (managerDTO == null) {
            throw new IllegalArgumentException("ManagerDTO cannot be null");
        }

        Manager newManager = managerMapper.toEntity(managerDTO);

        try {
            Manager savedManager = managerRepository.save(newManager);
            return managerMapper.toDto(savedManager);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save manager", e);
        }

    }

    @Override
    @Transactional
    public ManagerDTO findManagerById(UUID managerId) {
        return managerMapper.toDto(getById(managerId));
    }
    @Override
    public Manager getById(UUID managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found!"));
    }
    @Override
    public ManagerDTO updateManager(UUID managerId, ManagerDTO managerDTO) {
        Manager manager = getById(managerId);
        managerMapper.updateManagerDto(managerDTO, manager);
        return managerMapper.toDto(managerRepository.save(manager));
    }

    @Override
    public ManagerDTO deleteManager(UUID managerId) {
        Manager manager = getById(managerId);
        manager.setStatus(ManagerStatus.DELETED);
        managerRepository.save(manager);
        return managerMapper.toDto(manager);
    }

}

