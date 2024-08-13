package com.bankapp.app.controller;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/all")
    public List<TransactionDTO> findAll() {
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public TransactionDTO getTransactionDTO(@PathVariable("id") UUID id) {
        return transactionService.getTransactionDTO(id);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> transferTransactionDTO(@RequestBody TransactionDTO transaction) {
        TransactionDTO newTransaction = transactionService.transferTransactionDTO(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete_transfer/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable UUID id) {
        boolean deleteTransaction = transactionService.deleteTransaction(id);
        if (deleteTransaction) {
            return new ResponseEntity<>("Transaction deleted successfully!", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Transaction not found!", HttpStatus.NOT_FOUND);
        }
    }

}
