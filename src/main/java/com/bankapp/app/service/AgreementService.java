package com.bankapp.app.service;
import com.bankapp.app.dto.AgreementDTO;
import java.util.List;

public interface AgreementService {
    List<AgreementDTO> findAll();
    AgreementDTO getAgreementDTO(String id);
}
