package com.bankapp.app.service;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;

import java.util.UUID;

public interface ManagerService {
    Manager getById(UUID managerId);
    ManagerDTO findManagerById(UUID managerId);
    ManagerDTO createManager(ManagerDTO managerDTO);
}
