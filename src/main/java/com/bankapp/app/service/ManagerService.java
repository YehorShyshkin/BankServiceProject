package com.bankapp.app.service;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;

import java.util.List;
import java.util.UUID;

public interface ManagerService {
    List<ManagerDTO> findAll();
    Manager findManagerById(UUID managerId);

    Manager updateManager(UUID id, ManagerDTO managerDTO);
    List<ManagerDTO> findManagerDTOByIdList(UUID managerId);
    boolean deleteManager(UUID id);

    boolean mergeManagerAndClient(UUID clientId, UUID managerId);

    ManagerDTO createManager(Manager manager);

    ManagerDTO findManagerDTOById(UUID managerId);
}
