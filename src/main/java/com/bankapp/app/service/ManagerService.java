package com.bankapp.app.service;

import com.bankapp.app.dto.ManagerDTO;

import java.util.List;

public interface ManagerService {
    List<ManagerDTO> findAll();
    ManagerDTO getManagerDTO(String id);
}
