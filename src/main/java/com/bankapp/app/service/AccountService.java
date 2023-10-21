package com.bankapp.app.service;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;

import java.util.List;

public interface AccountService {
    Account getAccountById(String id);
    AccountDTO getAccountDTO(String id);
    List<AccountDTO> findAll();
    Account getById(String id);
}
