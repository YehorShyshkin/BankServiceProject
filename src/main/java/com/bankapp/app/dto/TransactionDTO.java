package com.bankapp.app.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionDTO {
    private String type;
    private BigDecimal amount;
    private String description;
}
