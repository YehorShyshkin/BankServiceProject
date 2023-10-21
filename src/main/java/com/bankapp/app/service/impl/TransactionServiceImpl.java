package com.bankapp.app.service.impl;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.mapper.TransactionMapper;
import com.bankapp.app.repository.TransactionRepository;
import com.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return transactionMapper.toDTO(transactionRepository.findById(UUID.fromString(id)).orElseThrow());
    }
}
