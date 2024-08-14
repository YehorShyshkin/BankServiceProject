package com.bankapp.app.service;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.model.Manager;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ManagerService {
    Manager getById(UUID managerId);
    ManagerDTO findManagerById(UUID managerId);
    ManagerDTO createManager(ManagerDTO managerDTO);

    ManagerDTO updateManager(UUID managerId, @Valid ManagerDTO managerDTO);

    ManagerDTO softDeleteManager(UUID managerId);

}
