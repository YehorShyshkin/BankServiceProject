package com.bankapp.app.controller;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.service.AccountService;
import com.bankapp.app.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    private final CardService cardService;
    // FIXME cardService declared but is not used


    @PostMapping("/creates")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDTO createAccount(@RequestBody AccountDTO accountDTO) {
        log.info("Creating account {}", accountDTO);
        return accountService.createAccount(accountDTO);
    }

    @GetMapping("/find/{accountsId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDTO findAccountById(@PathVariable UUID accountsId) {
        log.info("Finding account by id {}", accountsId);
        return accountService.findAccountById(accountsId);
    }

    // FIXME here in all other conrollers: it is not a good style to update model with get
    // all types of http request are intended for special purposes, GET usually to get sim info
    @GetMapping("/update/{accountsId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDTO updateAccount(
            @PathVariable UUID accountsId,
            @RequestBody AccountDTO accountDTO) {
        log.info("Updating account by id {}", accountsId);
        return accountService.updateAccount(accountsId, accountDTO);
    }

    // FIXME also not correct - must be DELETE
    @GetMapping("/delete/{accountsId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDTO softDeleteAccount(@PathVariable UUID accountsId) {
        log.info("Deleting account by id {}", accountsId);
        return accountService.softDeleteAccount(accountsId);
    }
}
