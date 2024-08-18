package com.bankapp.app.service.impl;


import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.exception.AccountNotFoundException;
import com.bankapp.app.exception.ClientNotFoundException;
import com.bankapp.app.mapper.AccountMapper;
import com.bankapp.app.model.Account;
import com.bankapp.app.model.Client;
import com.bankapp.app.model.enums.AccountStatus;
import com.bankapp.app.repository.AccountRepository;
import com.bankapp.app.repository.ClientRepository;
import com.bankapp.app.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public AccountDTO findAccountById(UUID accountsId) {
        return accountMapper.toDto(findById(accountsId));
    }

    @Override
    @Transactional
    public Account findById(UUID accountsId){
        return accountRepository.findById(accountsId)
                .orElseThrow(()-> new AccountNotFoundException(
                        String.format("Account with id %s not found", accountsId)));
    }

    @Override
    @Transactional
    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account newAccount = accountMapper.toEntity(accountDTO);
        Client client = clientRepository.findById(UUID.fromString(accountDTO.getClientId()))
                .orElseThrow(()-> new ClientNotFoundException("Client with id " + accountDTO.getClientId() + " not found"));

        newAccount.setClient(client);
        log.info("Created new account {}", newAccount);
        Account savedAccount = accountRepository.save(newAccount);
        return accountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountDTO updateAccount(UUID accountsId, AccountDTO accountDTO) {
        Account account = findById(accountsId);
        accountMapper.updateAccountFromDTO(accountDTO, account);
        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toDto(updatedAccount);
    }

    @Override
    @Transactional
    public AccountDTO softDeleteAccount(UUID accountsId) {
        Account account = findById(accountsId);
        account.setStatus(AccountStatus.DELETED);
        accountRepository.save(account);
        return accountMapper.toDto(account);
    }
}
