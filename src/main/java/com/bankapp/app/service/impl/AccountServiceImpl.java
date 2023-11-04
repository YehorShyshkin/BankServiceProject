package com.bankapp.app.service.impl;


import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.enums.AccountStatus;
import com.bankapp.app.enums.AccountType;
import com.bankapp.app.enums.CurrencyCode;
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
    public Account updateAccount(UUID id, AccountDTO accountDTO) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAccountName(accountDTO.getAccountName());
            account.setAccountType(AccountType.valueOf(accountDTO.getAccountType()));
            account.setAccountStatus(AccountStatus.valueOf(accountDTO.getAccountStatus()));
            account.setCurrencyCode(CurrencyCode.valueOf(accountDTO.getCurrencyCode()));
            account.setAccountBalance(new BigDecimal(accountDTO.getAccountBalance()));
            return accountRepository.save(account);
        } else {
            throw new EntityNotFoundException("Account not found");
        }
    }

    @Override
    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }

}
