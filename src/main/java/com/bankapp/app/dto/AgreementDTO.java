package com.bankapp.app.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
public class AgreementDTO {
    private BigDecimal interestRate;
    private String status;
    private BigDecimal sum;
    private String productId;
    private String accountId;
}
