package com.bankapp.app.controller;

import com.bankapp.app.dto.TransactionDTO;
import com.bankapp.app.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/all")
    public List<TransactionDTO> findAll(){
        return transactionService.findAll();
    }
    @GetMapping("/{id}")
    public TransactionDTO getTransactionDTO(@PathVariable("id") String id){
        return transactionService.getTransactionDTO(id);
    }


}
