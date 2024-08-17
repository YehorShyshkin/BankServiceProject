package com.bankapp.app.controller;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public TransactionDTO createTransaction(@RequestBody TransactionDTO transactionDTO) {
        log.info("Creating transaction: {}", transactionDTO);
        return transactionService.createTransaction(transactionDTO);
    }

    @GetMapping("/find/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionDTO getTransactionById(@PathVariable UUID transactionId) {
        log.info("Retrieving transaction by id: {}", transactionId);
        return transactionService.getTransactionById(transactionId);
    }

    @GetMapping("/delete/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionDTO deleteTransactionById(@PathVariable UUID transactionId) {
        log.info("Deleting transaction by id: {}", transactionId);
        return transactionService.deleteTransactionById(transactionId);
    }
}
