package com.bankapp.app.service;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.model.Agreement;

import java.util.UUID;

public interface AgreementService {

    AgreementDTO createAgreement(AgreementDTO agreementDTO);
    AgreementDTO findAgreementById(UUID agreementId);
    Agreement findById(UUID agreementId);
    AgreementDTO updateAgreement(UUID agreementId, AgreementDTO agreementDTO);
    AgreementDTO softDeleteAgreement(UUID agreementId);
}
