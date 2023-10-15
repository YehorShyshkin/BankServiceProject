package com.bankapp.app.service;

import com.bankapp.app.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    List<ClientDTO> findAll();
    ClientDTO getClientDTO(String id);

}
