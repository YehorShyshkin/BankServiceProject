package com.bankapp.app.service.impl;


import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.entity.Transaction;
import com.bankapp.app.mapper.AccountMapper;
import com.bankapp.app.repository.AccountRepository;
import com.bankapp.app.service.AccountService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public Account getAccountById(String id) {
        return accountRepository.findById(UUID.fromString(id)).orElseThrow();
    }

    @Override
    @Transactional
    public AccountDTO getAccountDTO(String id) {
        Optional<Account> accountOptional = accountRepository.findById(UUID.fromString(id));
        Account account = accountOptional.orElseThrow(() -> new NoSuchElementException("Account not found!"));
        return accountMapper.toDTO(account);
    }

    @Override
    @Transactional
    public List<AccountDTO> findAll() {
        return accountMapper.toDTO(accountRepository.findAll());
    }

    @Override
    @Transactional
    public Account getById(String id) {
        return accountRepository.getReferenceById(UUID.fromString(id));
    }


    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found!"));
    }

    @Override
    public Account updateAccount(UUID id, AccountDTO accountDTO) {
        Account currentAccount = findAccountById(id);
        Account updateAccount = accountMapper.updateAccountFromDTO(accountDTO, currentAccount);
        return accountRepository.save(updateAccount);
    }

    @Override
    public boolean deleteAccount(UUID id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public void updateBalance(Transaction transaction) {
        Account firstAccount = findAccountById(transaction.getTransactionDebitAccount().getId());
        Account secondAccount = findAccountById(transaction.getTransactionCreditAccount().getId());

        BigDecimal firstAccountBalance = firstAccount.getAccountBalance();
        BigDecimal secondAccountBalance = secondAccount.getAccountBalance();
        BigDecimal transactionAmount = transaction.getTransactionAmount();

        BigDecimal newFirstAccountBalance = firstAccountBalance.subtract(transactionAmount);
        BigDecimal newSecondAccountBalance = secondAccountBalance.add(transactionAmount);
        firstAccount.setAccountBalance(newFirstAccountBalance);
        secondAccount.setAccountBalance(newSecondAccountBalance);

        accountRepository.save(firstAccount);
        accountRepository.save(secondAccount);
    }
}
