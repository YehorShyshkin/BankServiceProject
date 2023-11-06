package com.bankapp.app.service;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;

import java.util.List;
import java.util.UUID;

public interface ManagerService {
    List<ManagerDTO> findAll();

    ManagerDTO getManagerDTO(String id);

    void save(Manager manager);

    Manager findManagerById(UUID managerId);

    Manager updateManager(UUID id, ManagerDTO managerDTO);

    boolean deleteManager(UUID id);

    boolean mergeManagerAndClient(UUID clientId, UUID managerId);
}
