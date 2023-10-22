package com.bankapp.app.controller;

import com.bankapp.app.dto.AccountDTO;
import com.bankapp.app.entity.Account;
import com.bankapp.app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/account")
    public ResponseEntity<String> get(@PathVariable("id") String id) {

        String uuidPattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
        if (!id.matches(uuidPattern)) {
            throw new IllegalArgumentException("ID is not a valid UUID");
        }
        String response = "RESPONSE: ID is a valid UUID: " + id;
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = "ERROR: " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(errorMessage);
    }
}
