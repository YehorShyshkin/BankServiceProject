package com.bankapp.app.service.impl;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.exception.AccountNotFoundException;
import com.bankapp.app.exception.TransactionNotFoundException;
import com.bankapp.app.mapper.TransactionMapper;
import com.bankapp.app.model.Account;
import com.bankapp.app.model.Transaction;
import com.bankapp.app.model.enums.TransactionType;
import com.bankapp.app.repository.AccountRepository;
import com.bankapp.app.repository.TransactionRepository;
import com.bankapp.app.service.AccountService;
import com.bankapp.app.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Transaction getById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(
                        String.format("Transaction with id %s not found", transactionId)));
    }

    @Override
    @Transactional
    public TransactionDTO getTransactionById(UUID transactionId) {
        return transactionMapper.toDto(getById(transactionId));
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Transaction newTransaction = transactionMapper.makeTransaction(transactionDTO);
        return transactionMapper.toDto(transactionRepository.save(newTransaction));
    }

    @Override
    @Transactional
    public TransactionDTO deleteTransactionById(UUID transactionId) {
        Transaction transaction = getById(transactionId);

        Account debitAccount = accountRepository.findById(transaction.getDebitAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException
                        ("Debit account not found"));
        Account creditAccount = accountRepository.findById(transaction.getCreditAccount().getId())
                .orElseThrow(() -> new AccountNotFoundException
                        ("Credit account not found"));

        debitAccount.setBalance(debitAccount.getBalance()
                .add(transaction.getAmount()));
        creditAccount.setBalance(creditAccount.getBalance()
                .subtract(transaction.getAmount()));

        accountRepository.save(debitAccount);
        accountRepository.save(creditAccount);

        transaction.setType(TransactionType.DELETED);
        transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);
    }

//
//    @Override
//    @Transactional
//    public void refundBalance(UUID transaction) {
//        Transaction originalTransaction = getTransactionById(transaction);
//        Transaction refundTransaction = new Transaction();
//        refundTransaction.setAmount(originalTransaction.getAmount().negate());
//        refundTransaction.setDebitAccount(originalTransaction.getDebitAccount());
//        refundTransaction.setCreditAccount(originalTransaction.getCreditAccount());
//        refundTransaction.setDescription("REFUND!");
//        refundTransaction.setType(TransactionType.REFUND);
////        accountService.updateBalance(refundTransaction);
//        transactionRepository.save(refundTransaction);
//    }
}
