package com.bankapp.app.service.impl;


import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.mapper.AccountMapper;
import com.bankapp.app.repository.AccountRepository;
import com.bankapp.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    @Override
    public Account getAccountById(String id) {
        return accountRepository.findById(UUID.fromString(id)).orElseThrow();
    }

    @Override
    public AccountDTO getAccountDTO(String id) {
        return accountMapper.toDo(accountRepository.findById(UUID.fromString(id)).orElseThrow());
    }

    @Override
    public List<AccountDTO> findAll() {
        return accountMapper.toDo(accountRepository.findAll());
    }

    @Override
    public Account getById(String id) {
        return accountRepository.getById(UUID.fromString(id));
    }
}
