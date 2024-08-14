package com.bankapp.app.service.impl;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.model.Account;
import com.bankapp.app.model.Agreement;
import com.bankapp.app.model.Product;
import com.bankapp.app.mapper.AgreementMapper;
import com.bankapp.app.repository.AgreementRepository;
import com.bankapp.app.service.AccountService;
import com.bankapp.app.service.AgreementService;
import com.bankapp.app.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
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
    private final AccountService accountService;
    private final ProductService productService;

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

    @Override
    public Agreement findAgreementById(UUID agreementId) {
        return agreementRepository.findById(agreementId)
                .orElseThrow(() -> new EntityNotFoundException("Agreement not found!"));
    }

    @Override
    public void save(Agreement agreement) {
        agreementRepository.save(agreement);
    }

    @Override
    public boolean deleteAgreement(UUID id) {
        if (agreementRepository.existsById(id)) {
            agreementRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Agreement updateAgreement(UUID id, AgreementDTO agreementDTO) {
        Agreement currentAgreement = findAgreementById(id);
        Agreement updateAgreement = agreementMapper.updateAgreementFromDTO(agreementDTO, currentAgreement);
        return agreementRepository.save(updateAgreement);
    }

    @Override
    public boolean mergeAgreementProductAndAccount(UUID accountId, UUID productId, UUID agreementId) {
        Agreement currentAgreement = findAgreementById(agreementId);
        Product currentProduct = productService.findProductById(productId);
        Account currentAccount = accountService.findAccountById(accountId);
        if (currentAgreement != null && currentProduct != null && currentAccount != null) {
            currentAgreement.setAccount(currentAccount);
            currentAgreement.setProduct(currentProduct);
            agreementRepository.save(currentAgreement);
            return true;
        } else {
            return false;
        }
    }
}
