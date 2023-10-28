package com.bankapp.app.service.impl;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.entity.Agreement;
import com.bankapp.app.mapper.AgreementMapper;
import com.bankapp.app.repository.AgreementRepository;
import com.bankapp.app.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;

    @Override
    public List<AgreementDTO> findAll() {
        return agreementMapper.toDTO(agreementRepository.findAll());
    }

    @Override
    public AgreementDTO getAgreementDTO(String id) {
        Optional<Agreement> agreementOptional = agreementRepository.findById(UUID.fromString(id));
        Agreement agreement = agreementOptional.orElseThrow(() -> new NoSuchElementException("Agreement not found"));
        return agreementMapper.toDTO(agreement);
    }
}
