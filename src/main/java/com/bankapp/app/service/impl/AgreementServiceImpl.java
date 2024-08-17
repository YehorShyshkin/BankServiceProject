package com.bankapp.app.service.impl;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.exception.AccountNotFoundException;
import com.bankapp.app.exception.AgreementNotFoundException;
import com.bankapp.app.exception.ProductNotFoundException;
import com.bankapp.app.mapper.AgreementMapper;
import com.bankapp.app.model.Account;
import com.bankapp.app.model.Agreement;
import com.bankapp.app.model.Product;
import com.bankapp.app.model.enums.AgreementStatus;
import com.bankapp.app.repository.AccountRepository;
import com.bankapp.app.repository.AgreementRepository;
import com.bankapp.app.repository.ProductRepository;
import com.bankapp.app.service.AgreementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgreementServiceImpl implements AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementMapper agreementMapper;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AgreementDTO findAgreementById(UUID agreementId) {
        return agreementMapper.toDto(findById(agreementId));
    }

    @Override
    @Transactional
    public Agreement findById(UUID agreementId) {
        return agreementRepository.findById(agreementId)
                .orElseThrow(() -> new AgreementNotFoundException(
                        (String.format("Agreement with id %s not found", agreementId))));
    }

    @Override
    @Transactional
    public AgreementDTO createAgreement(AgreementDTO agreementDTO) {
        Agreement newAgreement = agreementMapper.toEntity(agreementDTO);
        Product product = productRepository.findById(UUID.fromString(agreementDTO.getProductId()))
                .orElseThrow(() -> new ProductNotFoundException
                        (String.format("Product with id %s not found", agreementDTO.getProductId())));
        Account account = accountRepository.findById(UUID.fromString(agreementDTO.getAccountId()))
                        .orElseThrow(() -> new AccountNotFoundException
                                ("Account with id " + agreementDTO.getAccountId() + " not found"));
        newAgreement.setProduct(product);
        newAgreement.setAccount(account);
        log.info("Creating agreement {}", newAgreement);
        Agreement savedAgreement = agreementRepository.save(newAgreement);
        return agreementMapper.toDto(savedAgreement);
    }

    @Override
    @Transactional
    public AgreementDTO updateAgreement(UUID agreementId, AgreementDTO agreementDTO) {
        Agreement agreement = findById(agreementId);
        agreementMapper.updateAgreementFromDTO(agreementDTO, agreement);
        Agreement updatedAgreement = agreementRepository.save(agreement);
        return agreementMapper.toDto(updatedAgreement);
    }

    @Override
    public AgreementDTO softDeleteAgreement(UUID agreementId) {
        Agreement agreement = findById(agreementId);
        agreement.setStatus(AgreementStatus.DELETED);
        agreementRepository.save(agreement);
        return agreementMapper.toDto(agreement);
    }
}
