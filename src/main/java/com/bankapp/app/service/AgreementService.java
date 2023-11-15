package com.bankapp.app.service;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.entity.Agreement;

import java.util.List;

public interface AgreementService {
    List<AgreementDTO> findAll();
    AgreementDTO getAgreementDTO(String id);
    void save(Agreement agreement);
}
