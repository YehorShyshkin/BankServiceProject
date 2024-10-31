package com.bankapp.app.service.impl;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import com.bankapp.app.mapper.ManagerMapper;
import com.bankapp.app.repository.ManagerRepository;
import com.bankapp.app.service.ManagerService;
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


}
