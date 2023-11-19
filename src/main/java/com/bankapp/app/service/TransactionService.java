package com.bankapp.app.service;

import com.bankapp.app.dto.TransactionDTO;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionDTO> findAll();
    TransactionDTO getTransactionDTO(UUID id);
    TransactionDTO transferTransactionDTO(TransactionDTO transaction);
}
