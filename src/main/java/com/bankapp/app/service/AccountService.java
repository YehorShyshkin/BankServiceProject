package com.bankapp.app.service;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.model.Account;

import java.util.UUID;

public interface AccountService {
    AccountDTO createAccount(AccountDTO accountDTO);
    AccountDTO findAccountById(UUID accountsId);
    Account findById(UUID accountsId);
    AccountDTO updateAccount(UUID accountsId, AccountDTO accountDTO);
}

