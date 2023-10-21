package com.bankapp.app.controller;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/finding")
    public Account getAccountById(String id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/{id}")
    public AccountDTO getAccountDTO(@PathVariable("id") String id) {
        return accountService.getAccountDTO(id);
    }

    @GetMapping("/all")
    public List<AccountDTO> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/getuid")
    public Account getById(String id) {
        return accountService.getById(id);
    }
}
