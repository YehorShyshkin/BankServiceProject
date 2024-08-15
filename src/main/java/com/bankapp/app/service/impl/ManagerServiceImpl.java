package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.exception.ManagerNotFoundException;
import com.bankapp.app.mapper.ManagerMapper;
import com.bankapp.app.model.Manager;
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
    @Transactional
    public ManagerDTO createManager(ManagerDTO managerDTO) {
        Manager newManager = managerMapper.toEntity(managerDTO);
        Manager savedManager = managerRepository.save(newManager);
        return managerMapper.toDto(savedManager);
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerDTO findManagerById(UUID managerId) {
        return managerMapper.toDto(findById(managerId));
    }

    @Override
    @Transactional(readOnly = true)
    public Manager findById(UUID managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException
                        (String.format("Manager with id %s not found", managerId)));
    }

    @Override
    @Transactional
    public ManagerDTO updateManager(UUID managerId, ManagerDTO managerDTO) {
        Manager manager = findById(managerId);
        managerMapper.updateManagerFromDto(managerDTO, manager);
        return managerMapper.toDto(managerRepository.save(manager));
    }

    @Override
    @Transactional
    public ManagerDTO softDeleteManager(UUID managerId) {
        Manager manager = findById(managerId);
        manager.setStatus(ManagerStatus.DELETED);
        managerRepository.save(manager);
        return managerMapper.toDto(manager);
    }
}
