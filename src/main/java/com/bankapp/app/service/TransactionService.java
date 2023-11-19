package com.bankapp.app.service;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.entity.Transaction;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<TransactionDTO> findAll();
    TransactionDTO getTransactionDTO(UUID id);
    @Transactional
    Transaction getTransactionById(UUID uuid);
    TransactionDTO transferTransactionDTO(TransactionDTO transaction);
    boolean deleteTransaction(UUID id);
    @Transactional
    void refundBalance(UUID transaction);
}
