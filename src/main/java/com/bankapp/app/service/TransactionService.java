package com.bankapp.app.service;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.model.Transaction;

import java.util.UUID;

public interface TransactionService {
    TransactionDTO getTransactionById(UUID uuid);
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    Transaction getById(UUID transactionId);
    TransactionDTO deleteTransactionById(UUID transactionId);
}
