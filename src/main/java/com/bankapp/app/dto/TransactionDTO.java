package com.bankapp.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDTO {
    private String type;
    private BigDecimal amount;
    private String description;
    private String currencyCode;
    private String debitAccount;
    private String creditAccount;
}
