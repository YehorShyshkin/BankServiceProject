package com.bankapp.app.service.impl;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.entity.Transaction;
import com.bankapp.app.enums.TransactionType;
import com.bankapp.app.mapper.TransactionMapper;
import com.bankapp.app.repository.TransactionRepository;
import com.bankapp.app.service.AccountService;
import com.bankapp.app.service.TransactionService;
import jakarta.transaction.Transactional;
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
    private final AccountService accountService;

    @Override
    public List<TransactionDTO> findAll() {
        return transactionMapper.toDTO(transactionRepository.findAll());
    }

    @Override
    @Transactional
    public TransactionDTO getTransactionDTO(UUID id) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        Transaction transaction = transactionOptional.orElseThrow(() -> new NoSuchElementException("Transaction not found!"));
        return transactionMapper.toDTO(transaction);
    }

    @Override
    @Transactional
    public Transaction getTransactionById(UUID uuid) {
        return transactionRepository.findById(uuid).orElseThrow(() -> new NoSuchElementException("Transaction not found!"));
    }

    @Override
    @Transactional
    public TransactionDTO transferTransactionDTO(TransactionDTO transaction) {
        Transaction thisTransaction = transactionMapper.toTransaction(transaction);
        transactionRepository.save(thisTransaction);
        accountService.updateBalance(thisTransaction);
        return transaction;
    }

    @Override
    @Transactional
    public boolean deleteTransaction(UUID id) {
        if (transactionRepository.existsById(id)) {
            refundBalance(id);
            transactionRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void refundBalance(UUID transaction) {
        Transaction originalTransaction = getTransactionById(transaction);
        Transaction refundTransaction = new Transaction();
        refundTransaction.setTransactionAmount(originalTransaction.getTransactionAmount().negate());
        refundTransaction.setTransactionDebitAccount(originalTransaction.getTransactionCreditAccount());
        refundTransaction.setTransactionCreditAccount(originalTransaction.getTransactionDebitAccount());
        refundTransaction.setTransactionDescription("REFUND!");
        refundTransaction.setTransactionType(TransactionType.REFUND);
        accountService.updateBalance(refundTransaction);
        transactionRepository.save(refundTransaction);

    }
}
