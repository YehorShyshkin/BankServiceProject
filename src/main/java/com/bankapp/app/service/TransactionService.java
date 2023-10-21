package com.bankapp.app.service;

import com.bankapp.app.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> findAll();
    TransactionDTO getTransactionDTO(String id);

}
