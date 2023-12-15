package com.bankapp.app.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String name;
    private String status;
    private String currencyCode;
    private BigDecimal interestRate;
    private BigDecimal limit;
}
