package com.bankapp.app.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardStatusUpdateDTO {
    private String number;
    private BigDecimal balance;
    private String paymentSystem;
    private String status;
}
