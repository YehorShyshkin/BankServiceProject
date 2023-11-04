package com.bankapp.app.controller;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        if (!id.matches(uuidPattern)) {
            throw new IllegalArgumentException("ID is not a valid UUID");
        }
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

    @PostMapping("/create_account")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        accountService.save(account);
        return ResponseEntity.ok("Account was create! Success!");
    }

    @PostMapping("/update_account/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable UUID id, @RequestBody AccountDTO accountDTO) {
        Account updateAccount = accountService.updateAccount(id, accountDTO);
        if (updateAccount != null) {
            return new ResponseEntity<>(updateAccount, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
