package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import com.bankapp.app.exception.ManagerNotFoundException;
import com.bankapp.app.mapper.ManagerMapper;
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
        return managerMapper.toDto(managerRepository.save(newManager));
    }

    @Override
    public Manager getById(UUID managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException("Manager not found!"));
    }

    @Override
    @Transactional
    public ManagerDTO findManagerById(UUID managerId) {
        return managerMapper.toDto(getById(managerId));
    }
}
