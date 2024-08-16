package com.bankapp.app.service.impl;

import com.bankapp.app.dto.AgreementDTO;
import com.bankapp.app.exception.AgreementNotFoundException;
import com.bankapp.app.exception.ProductNotFoundException;
import com.bankapp.app.mapper.AgreementMapper;
import com.bankapp.app.model.Account;
import com.bankapp.app.model.Agreement;
import com.bankapp.app.model.Product;
import com.bankapp.app.repository.AgreementRepository;
import com.bankapp.app.repository.ProductRepository;
import com.bankapp.app.service.AccountService;
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
    private final AccountService accountService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public AgreementDTO findAgreementById(UUID agreementId) {
        return agreementMapper.toDto(findById(agreementId));
    }

    @Override
    @Transactional
    public Agreement findById(UUID agreementId){
        return agreementRepository.findById(agreementId)
                .orElseThrow(()-> new AgreementNotFoundException(
                        (String.format("Agreement with id %s not found", agreementId))));
    }

    @Override
    @Transactional
    public AgreementDTO createAgreement(AgreementDTO agreementDTO){
        Agreement newAgreement = agreementMapper.toEntity(agreementDTO);
        Product product = productRepository.findById(UUID.fromString(agreementDTO.getProductId()))
                .orElseThrow(()-> new ProductNotFoundException("Product with id %s not found"));
        Account account = accountService.findAccountById(UUID.fromString(agreementDTO.getAccountId()));
        newAgreement.setProduct(product);
        newAgreement.setAccount(account);
        log.info("Creating agreement {}", newAgreement);
        Agreement savedAgreement = agreementRepository.save(newAgreement);
        return agreementMapper.toDto(savedAgreement);
    }




//    @Override
//    public boolean mergeAgreementProductAndAccount(UUID accountId, UUID productId, UUID agreementId) {
//        Agreement currentAgreement = findAgreementById(agreementId);
////        Product currentProduct = productService.findProductById(productId);
////        Account currentAccount = accountService.findAccountById(accountId);
////        if (currentAgreement != null && currentProduct != null && currentAccount != null) {
////            currentAgreement.setAccount(currentAccount);
////            currentAgreement.setProduct(currentProduct);
////            agreementRepository.save(currentAgreement);
////            return true;
////        } else {
////            return false;
////        }
//        return true;
//    }
}
