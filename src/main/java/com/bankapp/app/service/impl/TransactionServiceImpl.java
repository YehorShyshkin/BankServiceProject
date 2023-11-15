package com.bankapp.app.service.impl;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.entity.Transaction;
import com.bankapp.app.mapper.TransactionMapper;
import com.bankapp.app.repository.TransactionRepository;
import com.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionDTO> findAll() {
        return transactionMapper.toDTO(transactionRepository.findAll());
    }

    @Override
    public TransactionDTO getTransactionDTO(String id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(UUID.fromString(id));
        Transaction transaction = transactionOptional.orElseThrow(() -> new NoSuchElementException("Transaction not found!"));
        return transactionMapper.toDTO(transaction);
    }

}
