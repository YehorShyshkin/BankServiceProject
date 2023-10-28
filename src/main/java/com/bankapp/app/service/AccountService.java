package com.bankapp.app.service;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    Account getAccountById(String id);
    AccountDTO getAccountDTO(String id);
    List<AccountDTO> findAll();
    Account getById(String id);
    //Account upsert(Account account);

    Account save(Account account);

    Account updateAccount(UUID id, AccountDTO accountDTO);

}
